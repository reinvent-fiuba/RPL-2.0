package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.Language;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ActivityCategoryResponseDTO {

    private Long id;

    private Long courseId;

    private String name;

    private String description;

    private Boolean active;

    private Long fileId;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static ActivityCategoryResponseDTO fromEntity(ActivityCategory activityCategory) {
        return ActivityCategoryResponseDTO.builder()
            .id(activityCategory.getId())
            .courseId(activityCategory.getCourse().getId())
            .name(activityCategory.getName())
            .description(activityCategory.getDescription())
            .active(activityCategory.getActive())
            .dateCreated(activityCategory.getDateCreated())
            .lastUpdated(activityCategory.getLastUpdated())
            .build();
    }
}
