package com.example.rpl.RPL.controller;

import static com.example.rpl.RPL.model.SubmissionStatus.ENQUEUED;
import static com.example.rpl.RPL.model.SubmissionStatus.PENDING;
import static com.example.rpl.RPL.model.SubmissionStatus.PROCESSING;

import com.example.rpl.RPL.controller.dto.ActivitySubmissionResponseDTO;
import com.example.rpl.RPL.controller.dto.ActivitySubmissionResultResponseDTO;
import com.example.rpl.RPL.controller.dto.AllFinalSubmissionsResponseDTO;
import com.example.rpl.RPL.controller.dto.SubmissionResultRequestDTO;
import com.example.rpl.RPL.controller.dto.UpdateSubmissionStatusRequestDTO;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.queue.IProducer;
import com.example.rpl.RPL.repository.TestRunRepository;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.AuthenticationService;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.service.TestService;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    private final SubmissionService submissionService;
    private final AuthenticationService authenticationService;
    private final TestService testService;

    private final TestRunRepository testRunRepository;


    @Autowired
    public SubmissionController(SubmissionService submissionService,
        AuthenticationService authenticationService,
        TestService testService, IProducer activitySubmissionQueueProducer,
        TestRunRepository testRunRepository) {
        this.submissionService = submissionService;
        this.authenticationService = authenticationService;
        this.testService = testService;
        this.testRunRepository = testRunRepository;
    }

    @GetMapping(value = "/api/submissions/{submissionId}")
    public ResponseEntity<ActivitySubmissionResponseDTO> getSubmission(
        @PathVariable Long submissionId) {
        log.error("SUBMISSION ID ID: {}", submissionId);

        ActivitySubmission as = submissionService.getActivitySubmission(submissionId);

//        GET UNIT TESTS
        UnitTest unitTest = testService.getUnitTests(as.getActivity().getId());

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

        ActivitySubmission as = submissionService
            .createSubmission(currentUser.getUser(), courseId, activityId, description,
                files);

        as = submissionService.postSubmissionToQueue(as);

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(as, null, List.of());
        return new ResponseEntity<>(asDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/submissions/{submissionId}/status")
    public ResponseEntity<ActivitySubmissionResponseDTO> updateSubmissionStatus(
        @PathVariable Long submissionId,
        @RequestBody @Valid UpdateSubmissionStatusRequestDTO updateSubmissionStatusRequestDTO) {
        ActivitySubmission activitySubmission = submissionService
            .updateSubmissionStatus(submissionId, updateSubmissionStatusRequestDTO.getStatus());

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(activitySubmission, null, List.of());
        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @PutMapping(value = "/api/courses/{courseId}/activities/{activityId}/submissions/{submissionId}/final")
    public ResponseEntity<ActivitySubmissionResponseDTO> setSubmissionAsFinalSolution(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId,
        @PathVariable Long submissionId) {
        ActivitySubmission activitySubmission = submissionService
            .setSubmissionAsFinalSolution(submissionId);

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(activitySubmission, null, List.of());
        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}/finalSubmission")
    public ResponseEntity<ActivitySubmissionResponseDTO> getFinalSubmission(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {

        ActivitySubmission as = submissionService
            .getFinalSubmission(activityId, currentUser.getUser());

        ActivitySubmissionResponseDTO asDto = ActivitySubmissionResponseDTO
            .fromEntity(as, null, List.of());

        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}/allFinalSubmissions")
    public ResponseEntity<AllFinalSubmissionsResponseDTO> getAllFinalSubmissionsFromActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {

        List<Long> finalSubmissionsFileIds = submissionService
            .getAllFinalSubmissionsFileIds(activityId);

        return new ResponseEntity<>(
            AllFinalSubmissionsResponseDTO.builder().submissionFileIds(finalSubmissionsFileIds)
                .build(), HttpStatus.OK);
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
                createSubmissionResultRequestDTO.getTestRunStdout(),
                createSubmissionResultRequestDTO.getTestRunUnitTestResult());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping(value = "/api/submissions/{submissionId}/result")
    public ResponseEntity<ActivitySubmissionResultResponseDTO> getSubmissionResult(
        @PathVariable Long submissionId) {
        log.error("GET SUBMISSION RESULT ID: {}", submissionId);

        ActivitySubmission as = submissionService.getActivitySubmission(submissionId);

        if (List.of(PENDING, ENQUEUED, PROCESSING).contains(as.getStatus())) {
            return new ResponseEntity<>(ActivitySubmissionResultResponseDTO.builder()
                .submissionStatus(as.getStatus().name()).build(), HttpStatus.NOT_FOUND);
        }

        TestRun run = testRunRepository
            .findTopByActivitySubmission_IdOrderByLastUpdatedDesc(submissionId);

//        GET UNIT TESTS
        UnitTest unitTest = testService.getUnitTests(as.getActivity().getId());

//        GET IO TESTSS
        List<IOTest> ioTests = testService.getAllIOTests(as.getActivity().getId());

        ActivitySubmissionResultResponseDTO asDto = ActivitySubmissionResultResponseDTO
            .fromEntity(as, unitTest, ioTests, run);

        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}/submissions")
    public ResponseEntity<List<ActivitySubmissionResultResponseDTO>> getAllSubmissionResults(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {
        log.error("COURSE ID: {}", courseId);
        log.error("ACTIVITY ID: {}", activityId);

        List<ActivitySubmissionResultResponseDTO> response = getSubmissionsFromStudentAndActivity(
            activityId, currentUser.getUser());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}/students/{studentId}/submissions")
    public ResponseEntity<List<ActivitySubmissionResultResponseDTO>> getAllSubmissionResultsFromStudent(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId, @PathVariable Long studentId) {
        log.error("COURSE ID: {}", courseId);
        log.error("ACTIVITY ID: {}", activityId);

        User student = authenticationService.getUserById(studentId);

        List<ActivitySubmissionResultResponseDTO> response = getSubmissionsFromStudentAndActivity(
            activityId, student);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private List<ActivitySubmissionResultResponseDTO> getSubmissionsFromStudentAndActivity(
        Long activityId, User student) {
        List<ActivitySubmission> submissions = submissionService
            .getAllSubmissionsByUserAndActivityId(student, activityId);

        return submissions.stream()
            .map(as -> {
                TestRun run = testRunRepository
                    .findTopByActivitySubmission_IdOrderByLastUpdatedDesc(as.getId());

//        GET UNIT TESTS
                UnitTest unitTest = testService.getUnitTests(as.getActivity().getId());

//        GET IO TESTSS
                List<IOTest> ioTests = testService.getAllIOTests(as.getActivity().getId());

                return ActivitySubmissionResultResponseDTO
                    .fromEntity(as, unitTest, ioTests, run);

            }).collect(Collectors.toList());
    }

    @PostMapping(value = "/api/submissions/reprocessAll")
    public ResponseEntity<List<ActivitySubmissionResponseDTO>> reprocessAllPendingSubmissions() {
        List<ActivitySubmission> submissions = submissionService.reprocessAllPendingSubmissions();

        List<ActivitySubmission> enqueuedSubmissions = submissions.stream()
            .map(submissionService::postSubmissionToQueue).collect(Collectors.toList());

        List<ActivitySubmissionResponseDTO> asDtos = enqueuedSubmissions.stream()
            .map(as -> ActivitySubmissionResponseDTO
                .fromEntity(as, null, List.of())).collect(Collectors.toList());
        return new ResponseEntity<>(asDtos, HttpStatus.CREATED);
    }
}
