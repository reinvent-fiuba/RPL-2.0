package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SubmissionResultRequestDTO {

    private String testRunResult;

    private String testRunStage;

    private String testRunExitMessage;

    private String testRunStderr;

    private String testRunStdout;

    @JsonCreator
    public SubmissionResultRequestDTO(
        @JsonProperty(value = "test_run_result", required = true) String testRunResult,
        @JsonProperty(value = "test_run_stage", required = true) String testRunStage,
        @JsonProperty(value = "test_run_stderr", required = true) String testRunStderr,
        @JsonProperty(value = "test_run_stdout", required = true) String testRunStdout,
        @JsonProperty(value = "test_run_exitMessage", required = true) String testRunExitMessage) {
        this.testRunResult = testRunResult;
        this.testRunStage = testRunStage;
        this.testRunStderr = testRunStderr;
        this.testRunStdout = testRunStdout;
        this.testRunExitMessage = testRunExitMessage;
    }
}
