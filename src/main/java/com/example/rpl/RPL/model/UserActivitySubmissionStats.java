package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.List;

@Getter
public class UserActivitySubmissionStats {

    private User user;

    private long totalSubmissions;

    private long successSubmissions;

    public UserActivitySubmissionStats(User user, List<ActivitySubmission> submissions) {
        this.user = user;
        this.totalSubmissions = submissions.size();
        this.successSubmissions = submissions.stream()
                .filter(activitySubmission ->
                        activitySubmission.getStatus() == SubmissionStatus.SUCCESS
                ).count();
    }
}
