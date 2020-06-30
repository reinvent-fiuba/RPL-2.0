package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitiesStats;
import com.example.rpl.RPL.model.ActivityStats;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ActivityStatsResponseDTO {

    private long activityId;

    private String name;

    private long activityCategoryId;

    private String activityCategoryName;

    private long totalSubmissions;

    private long points;

    private long successSubmissions;


    public static ActivityStatsResponseDTO fromEntity(ActivityStats activityStats) {
        return ActivityStatsResponseDTO.builder()
            .activityId(activityStats.getActivityId())
            .name(activityStats.getName())
            .activityCategoryId(activityStats.getActivityCategoryId())
            .activityCategoryName(activityStats.getActivityCategoryName())
            .totalSubmissions(activityStats.getTotalSubmissions())
            .points(activityStats.getPoints())
            .successSubmissions(activityStats.getSuccessSubmissions())
            .build();
    }
}
