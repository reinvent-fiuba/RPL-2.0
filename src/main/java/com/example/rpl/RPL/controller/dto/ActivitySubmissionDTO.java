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

    private String fileName;

    private String fileType;

    private Long fileId;

    private String activityLanguage;

    public static ActivitySubmissionDTO fromEntity(ActivitySubmission as) {
        return ActivitySubmissionDTO.builder()
            .id(as.getId())
            .fileName(as.getFile().getFileName())
            .fileType(as.getFile().getFileType())
            .fileId(as.getFile().getId())
            .activityLanguage(as.getActivity().getLanguage().getNameAndVersion())
            .build();
    }
}
