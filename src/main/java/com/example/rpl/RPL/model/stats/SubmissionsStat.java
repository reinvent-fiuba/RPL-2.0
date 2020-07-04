package com.example.rpl.RPL.model.stats;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.SubmissionStatus;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class SubmissionsStat {

    private long total;

    private long success;

    private long runtimeError;

    private long buildError;

    private long failure;

    public SubmissionsStat(List<ActivitySubmission> submissions) {
        this.total = submissions.size();
        Map<SubmissionStatus, Long> submissionsByStatus = submissions.stream()
                .collect(Collectors.groupingBy(submission -> submission.getStatus(), Collectors.counting()));
        this.success = submissionsByStatus.getOrDefault(SubmissionStatus.SUCCESS, (long) 0);
        this.runtimeError = submissionsByStatus.getOrDefault(SubmissionStatus.RUNTIME_ERROR, (long) 0);
        this.buildError = submissionsByStatus.getOrDefault(SubmissionStatus.BUILD_ERROR, (long) 0);
        this.failure = submissionsByStatus.getOrDefault(SubmissionStatus.FAILURE, (long) 0);
    }
}
