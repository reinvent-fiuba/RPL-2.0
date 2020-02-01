package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    public static ActivitySubmissionResultResponseDTO fromEntity(ActivitySubmission as,
        Optional<UnitTest> unitTest,
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
            .activityUnitTests("");

        unitTest.ifPresent(test -> ab.activityUnitTests = new String(test.getTestFile().getData()));

        ab.activityIOTests(ioTests.stream().map(IOTest::getTestIn).collect(Collectors.toList()));

        ab.submissionStatus(as.getStatus().name())
            .exitMessage(run.getExitMessage())
            .stderr(run.getStderr())
            .stdout(run.getStdout());

        return ab.build();
    }
}
