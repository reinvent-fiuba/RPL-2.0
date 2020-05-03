package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivityCategory;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

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
