package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivitySubmissionResultResponseDTO {

    private Long id;

    private String submissionFileName;

    private String submissionFileType;

    private Long submissionFileId;

    private String activityTestType;

    private String activitySupportingFileName;

    private String activitySupportingFileType;

    private Long activitySupportingFileId;

    private String activityLanguage;

    private String activityUnitTests;

    private List<String> activityIOTests;

    private String submissionStatus;

    private String exitMessage;

    private String stderr;

    private String stdout;

    private List<IOTestRunResultDTO> ioTestRunResults;

    private ZonedDateTime submissionDate;

    public static ActivitySubmissionResultResponseDTO fromEntity(ActivitySubmission as,
        UnitTest unitTest,
        List<IOTest> ioTests, TestRun run) {
        ActivitySubmissionResultResponseDTO.ActivitySubmissionResultResponseDTOBuilder ab = ActivitySubmissionResultResponseDTO
            .builder()
            .id(as.getId())
            .submissionFileName(as.getFile().getFileName())
            .submissionFileType(as.getFile().getFileType())
            .submissionFileId(as.getFile().getId())
            .activitySupportingFileName(as.getActivity().getSupportingFile().getFileName())
            .activitySupportingFileType(as.getActivity().getSupportingFile().getFileType())
            .activitySupportingFileId(as.getActivity().getSupportingFile().getId())
            .activityLanguage(as.getActivity().getLanguage().getNameAndVersion())
            .activityUnitTests("")
            .submissionStatus(as.getStatus().name())
            .submissionDate(as.getDateCreated());

        if (unitTest != null) {
            ab.activityUnitTests(new String(unitTest.getTestFile().getData()));
        }
        ab.activityIOTests(ioTests.stream().map(IOTest::getTestIn).collect(Collectors.toList()));

        if (run != null) {
            ab.exitMessage(run.getExitMessage())
                .stderr(run.getStderr())
                .stdout(run.getStdout());


        ab.ioTestRunResults(run.getIoTestRunList().stream().map(
            r -> new IOTestRunResultDTO(r.getId(), r.getTestIn(), r.getExpectedOutput(),
                r.getRunOutput())).collect(
            Collectors.toList()));
        }
        return ab.build();
    }

    @Getter
    @AllArgsConstructor
    private static class IOTestRunResultDTO {

        private Long id;
        private String testIn;
        private String expectedOutput;
        private String runOutput;
    }
}
