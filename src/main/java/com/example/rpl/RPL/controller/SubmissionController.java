package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivitySubmissionResponseDTO;
import com.example.rpl.RPL.controller.dto.SubmissionResultRequestDTO;
import com.example.rpl.RPL.controller.dto.UpdateSubmissionStatusRequestDTO;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.queue.IProducer;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.service.TestService;
import com.example.rpl.RPL.utils.TarUtils;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class SubmissionController {

    private SubmissionService submissionService;
    private TestService testService;
    private final IProducer activitySubmissionQueueProducer;

    @Autowired
    public SubmissionController(SubmissionService submissionService,
        TestService testService, IProducer activitySubmissionQueueProducer) {
        this.submissionService = submissionService;
        this.testService = testService;
        this.activitySubmissionQueueProducer = activitySubmissionQueueProducer;
    }

    @GetMapping(value = "/api/submissions/{submissionId}")
    public ResponseEntity<ActivitySubmissionResponseDTO> getSubmission(
        @PathVariable Long submissionId) {
        log.error("SUBMISSION ID ID: {}", submissionId);

        ActivitySubmission as = submissionService.getActivitySubmission(submissionId);

//        GET UNIT TESTS
        Optional<UnitTest> unitTest = testService.getUnitTests(as.getActivity().getId());

//        GET IO TESTSS
        List<IOTest> ioTests = testService.getAllIOTests(as.getActivity().getId());

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(as, unitTest, ioTests);

        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @PostMapping(value = "/api/courses/{courseId}/activities/{activityId}/submissions")
    public ResponseEntity<ActivitySubmissionResponseDTO> createSubmission(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId,
        @RequestParam(value = "description", required = false, defaultValue = "default description") String description,
        // Si bien ahora no se usa, puede servir para mandar metadata sobre el comprimido con los archivos
        @RequestParam("file") MultipartFile[] files) {
        log.info("COURSE ID: {}", courseId);
        log.info("ACTIVITY ID: {}", activityId);

        byte[] compressedFilesBytes = TarUtils.compressToTarGz(files);

        ActivitySubmission as = submissionService
            .createSubmission(currentUser.getUser(), courseId, activityId, description,
                compressedFilesBytes);

        // Submit submission ID to queue
        try {
            this.activitySubmissionQueueProducer
                .send(as.getId().toString(), as.getActivity().getLanguage().getNameAndVersion());
            as.setEnqueued();
        } catch (AmqpConnectException e) {
            log.error("Error sending submission ID to queue. Conection refused");
            log.error(e.getMessage());
        }

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(as, Optional.empty(), List.of());
        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }


    @PutMapping(value = "/api/submissions/{submissionId}")
    public ResponseEntity<ActivitySubmissionResponseDTO> updateSubmissionStatus(
        @PathVariable Long submissionId,
        @RequestBody @Valid UpdateSubmissionStatusRequestDTO updateSubmissionStatusRequestDTO) {
        ActivitySubmission activitySubmission = submissionService
            .updateSubmissionStatus(submissionId, updateSubmissionStatusRequestDTO.getStatus());

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(activitySubmission, Optional.empty(), List.of());
        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }


    @PostMapping(value = "/api/submissions/{submissionId}/result")
    public ResponseEntity<SubmissionResultRequestDTO> submitResults(@PathVariable Long submissionId,
        @RequestBody @Valid SubmissionResultRequestDTO createSubmissionResultRequestDTO) {

        submissionService
            .createSubmissionTestRun(submissionId,
                createSubmissionResultRequestDTO.getTestRunResult(),
                createSubmissionResultRequestDTO.getTestRunExitMessage(),
                createSubmissionResultRequestDTO.getTestRunStage(),
                createSubmissionResultRequestDTO.getTestRunStderr(),
                createSubmissionResultRequestDTO.getTestRunStdout());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}