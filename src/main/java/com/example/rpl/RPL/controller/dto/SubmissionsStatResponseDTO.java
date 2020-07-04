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

    public static SubmissionsStatResponseDTO fromEntity(SubmissionsStat submissionsStat) {
        return SubmissionsStatResponseDTO.builder()
            .total(submissionsStat.getTotal())
            .success(submissionsStat.getSuccess())
            .runtimeError(submissionsStat.getRuntimeError())
            .buildError(submissionsStat.getRuntimeError())
            .failure(submissionsStat.getRuntimeError())
            .build();
    }
}
