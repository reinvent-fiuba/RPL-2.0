package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.User;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivitySubmissionDTO {

    private Long id;

    private String submissionFileName;

    private String submissionFileType;

    private Long submissionFileId;

    private String activityTestType;

    private String activityFileName;

    private String activityFileType;

    private Long activityFileId;

    private String activityLanguage;

    private String activityUnitTests;

    private String activityIOTests;

    public static ActivitySubmissionDTO fromEntity(ActivitySubmission as) {
        ActivitySubmissionDTO.ActivitySubmissionDTOBuilder ab = ActivitySubmissionDTO.builder()
            .id(as.getId())
            .submissionFileName(as.getFile().getFileName())
            .submissionFileType(as.getFile().getFileType())
            .submissionFileId(as.getFile().getId())
            .activityFileName(as.getActivity().getFile().getFileName())
            .activityFileType(as.getActivity().getFile().getFileType())
            .activityFileId(as.getActivity().getFile().getId())
            .activityLanguage(as.getActivity().getLanguage().getNameAndVersion());

//        if (as.getActivity().get)
            return  ab.build();
    }
}
