package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ActivityStats {

    private long activityId;

    private String name;

    private long activityCategoryId;

    private String activityCategoryName;

    private long totalSubmissions;

    private long points;

    private long successSubmissions;

    public ActivityStats(Activity activity, List<ActivitySubmission> submissions) {
        this.activityId = activity.getId();
        this.name = activity.getName();
        this.activityCategoryId = activity.getActivityCategory().getId();
        this.activityCategoryName = activity.getActivityCategory().getName();
        this.totalSubmissions = submissions.size();
        this.points = activity.getPoints();
        this.successSubmissions = submissions.stream()
                .filter(activitySubmission ->
                        activitySubmission.getStatus() == SubmissionStatus.SUCCESS
                ).count();
    }
}
