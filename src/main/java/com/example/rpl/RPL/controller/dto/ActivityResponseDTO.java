package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.Language;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ActivityResponseDTO {

    private Long id;

    private Long courseId;

    private ActivityCategory activityCategory;

    private String name;

    private String description;

    private Language language;

    private Boolean active;

    private Long fileId;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static ActivityResponseDTO fromEntity(Activity activity) {
        return ActivityResponseDTO.builder()
            .id(activity.getId())
            .courseId(activity.getCourse().getId())
            .activityCategory(activity.getActivityCategory())
            .name(activity.getName())
            .description(activity.getDescription())
            .language(activity.getLanguage())
            .active(activity.getActive())
            .fileId(activity.getSupportingFile().getId())
            .dateCreated(activity.getDateCreated())
            .lastUpdated(activity.getLastUpdated())
            .build();
    }
}
