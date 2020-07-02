package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SubmissionStat {

    private long totalSubmissions;

    private long successSubmissions;

    public SubmissionStat(List<ActivitySubmission> submissions) {
        this.totalSubmissions = submissions.size();
        this.successSubmissions = submissions.stream()
                .filter(activitySubmission ->
                        activitySubmission.getStatus() == SubmissionStatus.SUCCESS
                ).count();
    }
}
