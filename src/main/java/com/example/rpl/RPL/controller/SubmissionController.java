package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivitySubmissionDTO;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.queue.Producer;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.service.TestService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class SubmissionController {

    private SubmissionService submissionService;
    private TestService testService;
    private final Producer activitySubmissionQueueProducer;

    @Autowired
    public SubmissionController(SubmissionService submissionService,
        TestService testService, Producer activitySubmissionQueueProducer) {
        this.submissionService = submissionService;
        this.testService = testService;
        this.activitySubmissionQueueProducer = activitySubmissionQueueProducer;
    }

    @GetMapping(value = "/api/submissions/{submissionId}")
    public ResponseEntity<ActivitySubmissionDTO> getSubmission(@PathVariable Long submissionId) {
        log.error("SUBMISSION ID ID: {}", submissionId);

        ActivitySubmission as = submissionService.getActivitySubmission(submissionId);

//        GET UNIT TESTS
        Optional<UnitTest> unitTest = testService.getUnitTests(as.getActivity().getId());

//        GET IO TESTSS
        List<IOTest> ioTests = testService.getAllIOTests(as.getActivity().getId());

        ActivitySubmissionDTO asDto = ActivitySubmissionDTO.fromEntity(as, unitTest, ioTests);

        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('course_create')")
    @PostMapping(value = "/api/courses/{courseId}/activities/{activityId}/submissions")
    public ResponseEntity<ActivitySubmissionDTO> createSubmission(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId,
        @RequestParam(value = "description", required = false, defaultValue = "default description") String description, // Si bien ahora no se usa, puede servir para mandar metadata sobre el comprimido con los archivos
        @RequestParam("file") MultipartFile file) {
        log.error("COURSE ID: {}", courseId);
        log.error("ACTIVITY ID: {}", activityId);

        ActivitySubmission as = submissionService.create(currentUser, courseId, activityId, description, file);


        // Submit submission ID to queue
        try {
            this.activitySubmissionQueueProducer.send(as.getId().toString(), as.getActivity().getLanguage().getNameAndVersion());
            as.setEnqueued();
        } catch (AmqpConnectException e) {
            log.error("Error sending submission ID to queue. Conection refused");
            log.error(e.getMessage());
        }

        ActivitySubmissionDTO asDto = ActivitySubmissionDTO.fromEntity(as, Optional.empty(), List.of());
        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }


    @PostMapping(value = "/api/uploadMultipleFiles")
    public String uploadMultipleFiles(@RequestParam("description") String[] descriptions,
        @RequestParam("file") MultipartFile[] files) {

        if (files.length != descriptions.length) {
            return "Mismatching no of files are equal to description";
        }

        String status = "";
        File dir = new File("/home/alepox/Desktop/pruebita_upload_files");
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String description = descriptions[i];
            try {
                byte[] bytes = file.getBytes();

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File uploadFile = new File(dir.getAbsolutePath()
                    + File.separator + file.getOriginalFilename());
                BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(uploadFile));
                outputStream.write(bytes);
                outputStream.close();

                status = status + "You successfully uploaded file=" + file.getOriginalFilename();
            } catch (Exception e) {
                status = status + "Failed to upload " + file.getOriginalFilename() + " " + e
                    .getMessage();
            }
        }
        return status;
    }

}