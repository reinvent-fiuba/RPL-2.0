package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.stats.SubmissionStat;
import lombok.Builder;
import lombok.Getter;

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


    public static ActivityStatsResponseDTO fromEntity(SubmissionStat submissionStat) {
        return ActivityStatsResponseDTO.builder()
//            .activityId(submissionStat.getActivityId())
//            .name(submissionStat.getName())
//            .activityCategoryId(submissionStat.getActivityCategoryId())
//            .activityCategoryName(submissionStat.getActivityCategoryName())
//            .totalSubmissions(submissionStat.getTotalSubmissions())
//            .points(submissionStat.getPoints())
//            .successSubmissions(submissionStat.getSuccessSubmissions())
            .build();
    }
}
