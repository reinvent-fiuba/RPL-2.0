package com.example.rpl.RPL.service;

import com.example.rpl.RPL.controller.dto.UnitTestResultDTO;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.ActivitySubmissionComment;
import com.example.rpl.RPL.model.IOTestRun;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.queue.IProducer;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.ActivitySubmissionCommentRepository;
import com.example.rpl.RPL.repository.ActivitySubmissionRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.TestRunRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class SubmissionService {

    private final TestService testService;

    private final ActivityRepository activityRepository;
    private final ActivitySubmissionRepository activitySubmissionRepository;
    private final FileRepository fileRepository;
    private final TestRunRepository testRunRepository;
    private final RplFilesService rplFilesService;
    private final IProducer activitySubmissionQueueProducer;
    private final ActivitySubmissionCommentRepository activitySubmissionCommentRepository;

    @Autowired
    public SubmissionService(TestService testService,
        ActivityRepository activityRepository,
        ActivitySubmissionRepository activitySubmissionRepository,
        FileRepository fileRepository,
        TestRunRepository testRunRepository,
        RplFilesService rplFilesService,
        IProducer activitySubmissionQueueProducer,
        ActivitySubmissionCommentRepository activitySubmissionCommentRepository) {
        this.testService = testService;
        this.activityRepository = activityRepository;
        this.activitySubmissionRepository = activitySubmissionRepository;
        this.fileRepository = fileRepository;
        this.testRunRepository = testRunRepository;
        this.rplFilesService = rplFilesService;
        this.activitySubmissionQueueProducer = activitySubmissionQueueProducer;
        this.activitySubmissionCommentRepository = activitySubmissionCommentRepository;
    }


    public ActivitySubmission getActivitySubmission(Long submissionId) {
        return activitySubmissionRepository.findById(submissionId).orElseThrow(
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
     * @param files Actual MultipartFile sent by the front-end (typically a
     */
    @Transactional
    public ActivitySubmission createSubmission(User user, Long courseId, Long activityId,
        String description, MultipartFile[] files) {

        Activity ac = activityRepository.findById(activityId).orElseThrow(
            () -> new NotFoundException("Activity not found",
                "activity_not_found"));

        byte[] compressedFilesBytes = new byte[0];
        try {
            compressedFilesBytes = rplFilesService
                .addAndOverwriteWithActivityFiles(files, ac.getStartingFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }

        RPLFile f = new RPLFile(String.format("%d_%d_%d", courseId, activityId, user.getId()),
            "application/gzip", compressedFilesBytes);

        f = fileRepository.save(f);

        ActivitySubmission as = new ActivitySubmission(ac, user, f,
            SubmissionStatus.PENDING);

        as = activitySubmissionRepository.save(as);

        return as;
    }

    /**
     * Creates a new submission run. That is, the result of running all the activity tests on the
     * submission files.
     *
     * @param testRunResult "OK" if we could execute the hole code or "ERROR" if we couldn't, also
     * TIME_OUT
     * @param testRunExitMessage Message describing why the program terminated if with "ERROR"
     * @param testRunStage "BUILD", "RUN", "COMPLETED". Only useful if error
     * @param testRunStderr stderr of the test run
     * @param testRunStdout stdout of the test run (WITH LOGGING)
     * @param testRunUnitTestResult results of the unit tests provided by unit test library
     */
    @Transactional
    public ActivitySubmission createSubmissionTestRun(Long submissionId, String testRunResult,
        String testRunExitMessage, String testRunStage, String testRunStderr,
        String testRunStdout,
        UnitTestResultDTO testRunUnitTestResult) {

        ActivitySubmission activitySubmission = this.getActivitySubmission(submissionId);

        TestRun testRun = new TestRun(activitySubmission, testRunResult.equals("OK"),
            testRunExitMessage, testRunStderr, testRunStdout);

        testRunRepository.save(testRun);

        if ("ERROR".equals(testRunResult)) {
            activitySubmission.setProcessedWithError(testRunStage);
            return activitySubmissionRepository.save(activitySubmission);
        }

        boolean passedTests = false;

        // Check if IO tests where correct
        if (activitySubmission.getActivity().getIsIOTested()) {
            List<IOTestRun> ioTestRuns = testService
                .parseAndSaveStdout(activitySubmission.getActivity().getId(), testRun);

            passedTests = testService
                .checkIfTestsPassed(activitySubmission.getActivity().getId(), ioTestRuns);
        } else if (testRunUnitTestResult != null) {
            // Check if Unit tests passed
            testService.saveUnitTestRuns(testRun, testRunUnitTestResult);
            passedTests =
                testRunUnitTestResult.getErrored() == 0 && testRunUnitTestResult.getFailed() == 0;
        }

        if (passedTests) {
            activitySubmission.setProcessedSuccess();
        } else {
            activitySubmission.setProcessedFailure();
        }

        // We override the overall status with TIME_OUT but we still want to save IO/Unit tests if
        // the TIME_OUT only happened in one IO/Unit test case
        if ("TIME_OUT".equals(testRunResult)) {
            activitySubmission.setProcessedWithTimeOut();
        }

        return activitySubmissionRepository.save(activitySubmission);
    }


    @Transactional
    public ActivitySubmission updateSubmissionStatus(Long submissionId,
        SubmissionStatus status) {
        ActivitySubmission activitySubmission = this.getActivitySubmission(submissionId);
        activitySubmission.setStatus(status);

        return activitySubmissionRepository.save(activitySubmission);
    }

    public List<ActivitySubmission> getAllSubmissionsByActivities(List<Activity> activities) {
        return activitySubmissionRepository.findAllByActivityIn(activities);
    }

    public List<ActivitySubmission> getAllSubmissionsByActivities(List<Activity> activities,
        Long userId) {
        return userId != null ?
            activitySubmissionRepository.findAllByActivityInAndUser_Id(activities, userId) :
            activitySubmissionRepository.findAllByActivityIn(activities);
    }

    List<ActivitySubmission> getAllSubmissionsByActivities(List<Activity> activities,
        Long userId, LocalDate date) {

        if (date != null) {
            ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault());
            return userId != null ?
                activitySubmissionRepository
                    .findAllByActivityInAndUser_IdAndDateCreatedBetween(activities, userId,
                        startOfDay, endOfDay) :
                activitySubmissionRepository
                    .findAllByActivityInAndDateCreatedBetween(activities, startOfDay, endOfDay);
        }
        return userId != null ?
            activitySubmissionRepository.findAllByActivityInAndUser_Id(activities, userId) :
            activitySubmissionRepository.findAllByActivityIn(activities);
    }

    public List<ActivitySubmission> getAllSubmissionsByCourseId(Long courseId) {
        return activitySubmissionRepository.findAllByActivity_Course_Id(courseId);
    }

    List<ActivitySubmission> getAllSubmissionsByCourseId(Long courseId, Long userId) {
        return userId != null ?
            activitySubmissionRepository.findAllByActivity_Course_IdAndUser_Id(courseId, userId) :
            activitySubmissionRepository.findAllByActivity_Course_Id(courseId);
    }

    List<ActivitySubmission> getAllSubmissionsByCourseId(Long courseId, Long userId,
        LocalDate date) {
        if (date != null) {
            ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault());
            return userId != null ?
                activitySubmissionRepository
                    .findAllByActivity_Course_IdAndUser_IdAndDateCreatedBetween(courseId, userId,
                        startOfDay, endOfDay) :
                activitySubmissionRepository
                    .findAllByActivity_Course_IdAndDateCreatedBetween(courseId, startOfDay,
                        endOfDay);

        }
        return this.getAllSubmissionsByCourseId(courseId, userId);
    }

    @Transactional
    public List<ActivitySubmission> getAllSubmissionsByUserAndActivities(User user,
        List<Activity> activities) {
        return activitySubmissionRepository.findAllByUserAndActivityIn(user, activities);
    }

    @Transactional
    public List<ActivitySubmission> getAllSubmissionsByUserAndActivityId(User user,
        Long activityId) {
        return activitySubmissionRepository.findAllByUserAndActivity_Id(user, activityId);
    }

    @Transactional
    public ActivitySubmission setSubmissionAsFinalSolution(Long submissionId) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        submission.setAsFinalSolution();

        return activitySubmissionRepository.save(submission);
    }

    @Transactional
    public ActivitySubmission getFinalSubmission(Long activityId, User user) {
        return activitySubmissionRepository
            .findByActivity_IdAndUserIdAndIsFinalSolution(activityId, user.getId(), true)
            .orElseThrow(() -> new NotFoundException("Activity submission not found",
                "final_submission_not_found"));
    }

    @Transactional
    public List<Long> getAllFinalSubmissionsFileIds(Long activityId) {
        return activitySubmissionRepository.findByActivity_IdAndIsFinalSolution(activityId, true)
            .stream()
            .map(ActivitySubmission::getFile).map(RPLFile::getId).collect(Collectors.toList());
    }

    @Transactional
    public List<ActivitySubmission> reprocessAllPendingSubmissions() {
        List<ActivitySubmission> processingStuckSubmissions = activitySubmissionRepository
            .findAllByStatus(SubmissionStatus.PROCESSING);
        List<ActivitySubmission> pendingSubmissions = activitySubmissionRepository
            .findAllByStatus(SubmissionStatus.PENDING);

        processingStuckSubmissions.addAll(pendingSubmissions);
        return processingStuckSubmissions;
    }

    @Transactional
    public ActivitySubmission postSubmissionToQueue(ActivitySubmission as) {
        try {
            this.activitySubmissionQueueProducer
                .send(as.getId().toString(),
                    as.getActivity().getLanguage().getNameAndVersion());
            as.setEnqueued();
        } catch (AmqpConnectException e) {
            log.error("Error sending submission ID to queue. Connection refused");
            log.error(e.getMessage());
        }
        return activitySubmissionRepository.save(as);
    }

    @Transactional
    public List<ActivitySubmissionComment> addSubmissionComment(User author, Long submissionId,
        String comment) {

        ActivitySubmission submission = this.getActivitySubmission(submissionId);

//        Users can only see submissions marked as shared :)
//        if (!submission.getIsShared()) {
//            throw new NotFoundException("Activity submission not shared",
//                "final_submission_not_shared");
//        }

        ActivitySubmissionComment submissionComment = new ActivitySubmissionComment(
            submission.getActivity(), submission, author, comment);

        activitySubmissionCommentRepository.save(submissionComment);

        return activitySubmissionCommentRepository.findAllByActivitySubmission(submission);
    }

    @Transactional
    public List<ActivitySubmissionComment> deleteSubmissionComment(User user, Boolean isTeacher,
        Long submissionId,
        Long commentId) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        ActivitySubmissionComment comment = activitySubmissionCommentRepository.findById(commentId)
            .orElseThrow(
                () -> new NotFoundException("Comment not found",
                    "activity_submission_comment_not_found"));

        if (user.getId().equals(comment.getAuthor().getId()) || isTeacher) {
            activitySubmissionCommentRepository.delete(comment);
        }

        return activitySubmissionCommentRepository.findAllByActivitySubmission(submission);
    }

    @Transactional
    public ActivitySubmission updateSubmission(Long submissionId, SubmissionStatus status,
        Boolean isFinal, Boolean isShared) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        if (status != null) {
            submission = this.updateSubmissionStatus(submissionId, status);
        }

        if (isFinal != null) {
            submission = this.setSubmissionAsFinalSolution(submissionId);
        }

        if (isShared != null) {
            submission = this.setSubmissionAsShared(submissionId);
        }

        return submission;
    }

    @Transactional
    public ActivitySubmission setSubmissionAsShared(Long submissionId) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        submission.setShared();

        return activitySubmissionRepository.save(submission);
    }
}
