package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.BadRequestException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.SubmissionRepository;
import com.example.rpl.RPL.repository.TestRunRepository;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class SubmissionService {

    private TestService testService;

    private ActivityRepository activityRepository;
    private SubmissionRepository submissionRepository;
    private FileRepository fileRepository;
    private TestRunRepository testRunRepository;

    @Autowired
    public SubmissionService(TestService testService,
        ActivityRepository activityRepository,
        SubmissionRepository submissionRepository,
        FileRepository fileRepository,
        TestRunRepository testRunRepository) {
        this.testService = testService;
        this.activityRepository = activityRepository;
        this.submissionRepository = submissionRepository;
        this.fileRepository = fileRepository;
        this.testRunRepository = testRunRepository;
    }


    public ActivitySubmission getActivitySubmission(Long submissionId) {
        return submissionRepository.findById(submissionId).orElseThrow(
            () -> new NotFoundException("Activity submission not found",
                "activity_submission_not_found"));
    }


    /**
     * Creates a new submission by a user for a given Activity.
     *
     * @param user The submitter of the assignment
     * @param courseId CourseId, for logging purposes
     * @param activityId The activity the user wants to complete
     * @param description File description
     * @param file Actual MultipartFile sent by the front-end (typically a compressed tar.gz with
     * all the submission files)
     */
    @Transactional
    public ActivitySubmission create(User user, Long courseId, Long activityId,
        String description, MultipartFile file) {

        Activity ac = activityRepository.findById(activityId).orElseThrow(
            () -> new NotFoundException("Activity not found",
                "activity_not_found"));

        try {
            RPLFile f = new RPLFile(String.format("%d_%d_%d", courseId, activityId, user.getId()),
                file.getContentType(), file.getBytes());

            f = fileRepository.save(f);

            ActivitySubmission as = new ActivitySubmission(ac, user, f,
                SubmissionStatus.PENDING);

            as = submissionRepository.save(as);

            return as;

        } catch (IOException e) {
            log.error("ERROR OBTENIENDO LOS BYTES DE LA SUBMISSION");
            log.error(e.getMessage());
            throw new BadRequestException("Error obteniendo los bytes de la submission",
                "bad_file");
        }
    }

    /**
     * Creates a new submission run. That is, the result of running all the activity tests on the
     * submission files.
     *  @param testRunResult "OK" or "ERROR"
     * @param testRunExitMessage Message describing why the program terminated if with "ERROR"
     * @param testRunStage "BUILD", "RUN", "COMPLETED". Only useful if error
     * @param testRunStderr stderr of the test run
     * @param testRunStdout stdout of the test run (WITH LOGGING)
     * @return
     */
    @Transactional
    public ActivitySubmission createSubmissionTestRun(Long submissionId, String testRunResult,
        String testRunExitMessage, String testRunStage, String testRunStderr,
        String testRunStdout) {

        ActivitySubmission activitySubmission = this.getActivitySubmission(submissionId);

        TestRun testRun = new TestRun(activitySubmission, testRunResult.equals("OK"),
            testRunExitMessage, testRunStderr, testRunStdout);

        testRunRepository.save(testRun);

        if ("ERROR".equals(testRunResult)) {
            activitySubmission.setProcessedWithError(testRunStage);
            return submissionRepository.save(activitySubmission);
        }

        // Check if tests where correct
        if (testService
            .checkIfTestsPassed(activitySubmission.getActivity().getId(), testRunStdout)) {
            activitySubmission.setProcessedSuccess();
        } else {
            activitySubmission.setProcessedFailure();
        }

        return submissionRepository.save(activitySubmission);
    }


    public ActivitySubmission updateSubmissionStatus(Long submissionId,
        SubmissionStatus status) {
        ActivitySubmission activitySubmission = this.getActivitySubmission(submissionId);
        activitySubmission.setStatus(status);

        return submissionRepository.save(activitySubmission);
    }

    public List<ActivitySubmission> getAllSubmissionsByUserAndActivities(User user, List<Activity> activities) {
        return submissionRepository.findAllByUserAndActivityIn(user, activities);
    }
}
