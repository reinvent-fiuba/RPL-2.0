package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.stats.SubmissionsStat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionsStatResponseDTO {

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

    public static SubmissionsStatResponseDTO fromEntity(SubmissionsStat submissionsStat) {
        return SubmissionsStatResponseDTO.builder()
            .total(submissionsStat.getTotal())
            .success(submissionsStat.getSuccess())
            .runtimeError(submissionsStat.getRuntimeError())
            .buildError(submissionsStat.getBuildError())
            .failure(submissionsStat.getFailure())
            .avgSubmissionsByStudent(submissionsStat.getAvgSubmissionsByStudent())
            .avgErrorSubmissionsByStudent(submissionsStat.getAvgErrorSubmissionsByStudent())
            .avgSuccessSubmissionsByStudent(submissionsStat.getAvgSuccessSubmissionsByStudent())
            .totalStudents(submissionsStat.getTotalStudents())
            .totalStudentsSuccess(submissionsStat.getTotalStudentsSuccess())
            .totalStudentsError(submissionsStat.getTotalStudentsError())
            .build();
    }
}
