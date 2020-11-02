package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivitySubmissionResponseDTO {

    private Long id;

    private String submissionFileName;

    private String submissionFileType;

    private Long submissionFileId;

    private String activityTestType;

    private String activityStartingFilesName;

    private String activityStartingFilesType;

    private Long activityStartingFilesId;

    private String activityLanguage;

    private Boolean isIOTested;

    private String activityUnitTestsContent;

    private String compilationFlags;

    private List<String> activityIOTests;

    private Boolean isFinalSolution;

    private String shareUrl;

    public static ActivitySubmissionResponseDTO fromEntity(ActivitySubmission as,
        UnitTest unitTest,
        List<IOTest> ioTests) {
        ActivitySubmissionResponseDTO.ActivitySubmissionResponseDTOBuilder ab = ActivitySubmissionResponseDTO
            .builder()
            .id(as.getId())
            .submissionFileName(as.getFile().getFileName())
            .submissionFileType(as.getFile().getFileType())
            .submissionFileId(as.getFile().getId())
            .activityStartingFilesName(as.getActivity().getStartingFiles().getFileName())
            .activityStartingFilesType(as.getActivity().getStartingFiles().getFileType())
            .activityStartingFilesId(as.getActivity().getStartingFiles().getId())
            .activityLanguage(as.getActivity().getLanguage().getNameAndVersion())
            .isIOTested(as.getActivity().getIsIOTested())
            .compilationFlags(as.getActivity().getCompilationFlags())
            .activityUnitTestsContent("")
            .isFinalSolution(as.getIsFinalSolution())
            .shareUrl(as.getShareUrl());

        if (unitTest != null) {
            ab.activityUnitTestsContent(new String(unitTest.getTestFile().getData()));
        }
        ab.activityIOTests(ioTests.stream().map(IOTest::getTestIn).collect(Collectors.toList()));

        return ab.build();
    }
}
