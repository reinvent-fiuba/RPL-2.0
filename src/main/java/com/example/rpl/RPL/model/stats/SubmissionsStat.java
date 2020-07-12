package com.example.rpl.RPL.model.stats;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.User;
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

    private double avgSubmissionsByStudent;

    private double avgErrorSubmissionsByStudent;

    private double avgSuccessSubmissionsByStudent;

    private long totalStudents;

    private long totalStudentsSuccess;

    private long totalStudentsError;

    public SubmissionsStat(List<ActivitySubmission> submissions) {
        this.total = submissions.size();
        Map<SubmissionStatus, Long> submissionsByStatus = submissions.stream()
                .collect(Collectors.groupingBy(submission -> submission.getStatus(), Collectors.counting()));
        Map<User, List<ActivitySubmission>> submissionsByUser = submissions.stream()
                .collect(Collectors.groupingBy(submission -> submission.getUser()));
        this.success = submissionsByStatus.getOrDefault(SubmissionStatus.SUCCESS, (long) 0);
        this.runtimeError = submissionsByStatus.getOrDefault(SubmissionStatus.RUNTIME_ERROR, (long) 0);
        this.buildError = submissionsByStatus.getOrDefault(SubmissionStatus.BUILD_ERROR, (long) 0);
        this.failure = submissionsByStatus.getOrDefault(SubmissionStatus.FAILURE, (long) 0);

        this.totalStudents = submissionsByUser.size();
        this.avgSubmissionsByStudent = this.totalStudents != 0 ? ((double) this.total)/this.totalStudents : 0;
        this.avgErrorSubmissionsByStudent = this.totalStudents != 0 ? ((double) (this.total - this.success))/this.totalStudents : 0;
        this.avgSuccessSubmissionsByStudent = this.totalStudents != 0 ? ((double) this.success)/totalStudents : 0;

        this.totalStudentsSuccess = 0;
        this.totalStudentsError = 0;

        for (User user: submissionsByUser.keySet()) {
            if (submissionsByUser.get(user).stream()
                    .anyMatch(submission -> submission.getStatus() == SubmissionStatus.SUCCESS)) {
                this.totalStudentsSuccess++;
            } else {
                this.totalStudentsError++;
            }
        }
    }
}
