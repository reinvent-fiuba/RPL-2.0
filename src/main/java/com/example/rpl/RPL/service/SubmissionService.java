package com.example.rpl.RPL.service;

//import com.example.rpl.RPL.client.GitHubClient;

import com.example.rpl.RPL.client.GithubClientService;
import com.example.rpl.RPL.client.dto.GistFileDTO;
import com.example.rpl.RPL.controller.dto.UnitTestResultDTO;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTestRun;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTestRun;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.queue.IProducer;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.SubmissionRepository;
import com.example.rpl.RPL.repository.TestRunRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.rpl.RPL.repository.specification.SubmissionSpecifications.*;

@Slf4j
@Service
public class SubmissionService {

    private final TestService testService;

    private final ActivityRepository activityRepository;
    private final SubmissionRepository submissionRepository;
    private final FileRepository fileRepository;
    private final TestRunRepository testRunRepository;
    private final RplFilesService rplFilesService;
    private final IProducer activitySubmissionQueueProducer;
    private final GithubClientService GHClient;

    @Autowired
    public SubmissionService(TestService testService,
        ActivityRepository activityRepository,
        SubmissionRepository submissionRepository,
        FileRepository fileRepository,
        TestRunRepository testRunRepository,
        RplFilesService rplFilesService,
        IProducer activitySubmissionQueueProducer,
        GithubClientService ghClient) {
        this.testService = testService;
        this.activityRepository = activityRepository;
        this.submissionRepository = submissionRepository;
        this.fileRepository = fileRepository;
        this.testRunRepository = testRunRepository;
        this.rplFilesService = rplFilesService;
        this.activitySubmissionQueueProducer = activitySubmissionQueueProducer;
        this.GHClient = ghClient;
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

        as = submissionRepository.save(as);

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
            return submissionRepository.save(activitySubmission);
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

        return submissionRepository.save(activitySubmission);
    }


    @Transactional
    public ActivitySubmission updateSubmissionStatus(Long submissionId,
        SubmissionStatus status) {
        ActivitySubmission activitySubmission = this.getActivitySubmission(submissionId);
        activitySubmission.setStatus(status);

        return submissionRepository.save(activitySubmission);
    }

    List<ActivitySubmission> getAllSubmissionsByActivities(List<Activity> activities,
        Long userId, LocalDate date) {

        if (activities != null && activities.size() == 0) {
            return new ArrayList<>();
        }

        Specification<ActivitySubmission> specification = Specification
                .where(activities == null ? null : activitiesIn(activities))
                .and(userId == null ? null : userIdIs(userId))
                .and(date == null ? null : dateCreatedIs(date));

        return submissionRepository.findAll(specification);
    }

    List<ActivitySubmission> search(Long courseId, Long userId, Long activityId, Long categoryId,
                                    LocalDate date) {

        Specification<ActivitySubmission> specification = Specification
                .where(courseId == null ? null : courseIdIs(courseId))
                .and(userId == null ? null : userIdIs(userId))
                .and(date == null ? null : dateCreatedIs(date))
                .and(activityId == null ? null : activityIdIs(activityId))
                .and(categoryId == null ? null : categoryIdIs(categoryId));

        return submissionRepository.findAll(specification);
    }

    @Transactional
    public List<ActivitySubmission> getAllSubmissionsByUserAndActivities(User user,
        List<Activity> activities) {
        return submissionRepository.findAllByUserAndActivityIn(user, activities);
    }

    @Transactional
    public List<ActivitySubmission> getAllSubmissionsByUserAndActivityId(User user,
        Long activityId) {
        return submissionRepository.findAllByUserAndActivity_Id(user, activityId);
    }

    @Transactional
    public ActivitySubmission setSubmissionAsFinalSolution(Long submissionId) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        submission.setAsFinalSolution();

        return submissionRepository.save(submission);
    }

    @Transactional
    public ActivitySubmission getFinalSubmission(Long activityId, User user) {
        return submissionRepository
            .findByActivity_IdAndUserIdAndIsFinalSolution(activityId, user.getId(), true)
            .orElseThrow(() -> new NotFoundException("Activity submission not found",
                "final_submission_not_found"));
    }

    @Transactional
    public List<Long> getAllFinalSubmissionsFileIds(Long activityId) {
        return submissionRepository.findByActivity_IdAndIsFinalSolution(activityId, true).stream()
            .map(ActivitySubmission::getFile).map(RPLFile::getId).collect(Collectors.toList());
    }

    @Transactional
    public List<ActivitySubmission> reprocessAllPendingSubmissions() {
        List<ActivitySubmission> processingStuckSubmissions = submissionRepository
            .findAllByStatus(SubmissionStatus.PROCESSING);
        List<ActivitySubmission> pendingSubmissions = submissionRepository
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
        return submissionRepository.save(as);
    }

    public ActivitySubmission shareSubmission(Long submissionId) {
        ActivitySubmission submission = this.getActivitySubmission(submissionId);

        TestRun run = testRunRepository.findByActivitySubmission_Id(submissionId);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            GistFileDTO fileStderr = GistFileDTO.builder()//.filename("stderr.txt")
                .content(run.getStderr()).build();

            GistFile fileStderrGF = new GistFile();
            fileStderrGF.setContent(run.getStderr());

            GistFileDTO fileStdout = GistFileDTO.builder()//.filename("stdout.txt")
                .content(run.getStdout()).build();

            GistFile fileStdoutGF = new GistFile();
            fileStdoutGF.setContent(run.getStdout());

            //                    objectMapper.writeValueAsString(
            GistFileDTO fileIOTestResult = GistFileDTO.builder()
                .content(
                    run.getIoTestRunList().stream().map(
                        IOTestRun::buildMarkdownRepresentation

//                                Map.of(
//                                "test_name", r.getTestName(),
//                                "test_input", r.getTestIn(),
//                                "test_expected_output", r.getExpectedOutput(),
//                                "test_actual_output", r.getRunOutput())
                    ).collect(
                        Collectors.joining("\n\n----\n\n"))
                ).build();

            GistFile fileIOTestResultGF = new GistFile();
            fileIOTestResultGF.setContent(run.getIoTestRunList().stream().map(
                IOTestRun::buildMarkdownRepresentation

//                                Map.of(
//                                "test_name", r.getTestName(),
//                                "test_input", r.getTestIn(),
//                                "test_expected_output", r.getExpectedOutput(),
//                                "test_actual_output", r.getRunOutput())
            ).collect(
                Collectors.joining("\n\n----\n\n")));

            GistFile fileUnitTestResultGF = new GistFile();

            fileUnitTestResultGF.setContent(run.getUnitTestRunList().stream().map(
                UnitTestRun::buildMarkdownRepresentation
//                                Map.of(
//                                "test_name", r.getName(),
//                                "passed", r.getPassed(),
//                                "errors", r.getErrorMessages())
            )
                .collect(
                    Collectors.joining("\n----\n")));

            GistFileDTO fileUnitTestResult = GistFileDTO.builder()
                .content(
//                    objectMapper.writeValueAsString(
                    run.getUnitTestRunList().stream().map(
                        UnitTestRun::buildMarkdownRepresentation
//                                Map.of(
//                                "test_name", r.getName(),
//                                "passed", r.getPassed(),
//                                "errors", r.getErrorMessages())
                    )
                        .collect(
                            Collectors.joining("\n----\n"))
                ).build();

            Map<String, GistFileDTO> files = new HashMap<>();

            Map<String, GistFile> filesGF = new HashMap<>();

            if (!fileStderr.getContent().isEmpty()) {
                files.put("stderr.txt", fileStderr);
                filesGF.put("stderr.txt", fileStderrGF);
            }

            if (!fileStdout.getContent().isEmpty()) {
                files.put("stdout.txt", fileStdout);
                filesGF.put("stdout.txt", fileStdoutGF);
            }

            if (!run.getUnitTestRunList().isEmpty()) {
                files.put("unit_test_run_result.md", fileUnitTestResult);
                filesGF.put("unit_test_run_result.md", fileUnitTestResultGF);
            }

            if (!run.getIoTestRunList().isEmpty()) {
                files.put("io_test_run_result.md", fileIOTestResult);
                filesGF.put("io_test_run_result.md", fileIOTestResultGF);
            }

            Map<String, String> submissionFiles = rplFilesService
                .extractFileForStudent(submission.getFile().getId());

            submissionFiles.remove("files_metadata");

            submissionFiles.forEach((filename, content) -> {
                    files.put(filename,
                        GistFileDTO.builder()
//                        .filename(filename)
                            .content(content.isEmpty() ? "---" : content).build());

                    GistFile gf = new GistFile();
                    gf.setContent(content.isEmpty() ? "---" : content);
                    filesGF.put(filename, gf);
                }
            );

//            GitHubClient client = new GitHubClient();
//
//            String gistUrl = client.createGist("demo description", files);

            //OAuth2 token authentication
//            GitHubClient client = new GitHubClient();
//            client.setOAuth2Token("SlAV32hkKG");

//            GistFile file = new GistFile();
//            file.setContent("System.out.println(\"Hello World\");");

            Gist gist = new Gist();
            gist.setDescription("Prints a string to standard out");
            gist.setFiles(filesGF);

            GistService service = new GistService();
            service.getClient().setOAuth2Token("a8ec3a8910959f77e88b7d71742940c42f6cb1c0");
            gist = service.createGist(gist); //returns the created gist

            log.info("Created gist with id {}", gist.getHtmlUrl());
            submission.setShareLink(gist.getHtmlUrl());
//
//
//            log.info("Created gist with id {}", gistUrl);
//            submission.setShareLink(gistUrl);
            return submissionRepository.save(submission);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al crear gist: {}", e.toString());
        }
        return submission;
    }
}
