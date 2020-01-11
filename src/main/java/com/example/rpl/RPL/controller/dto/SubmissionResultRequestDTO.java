package com.example.rpl.RPL.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionResultRequestDTO {

    private Long id;

    private String submissionId;

    private String testRunResult;

    private String testRunStage;

    private String testRunExitMessage;

    private String testRunStderr;

    private String testRunStdout;


    public static SubmissionResultRequestDTO fromEntity() {

        return SubmissionResultRequestDTO.builder().build();
    }
}
