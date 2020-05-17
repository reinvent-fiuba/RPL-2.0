package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SubmissionResultRequestDTO {

    private String testRunResult;

    private String testRunStage;

    private String testRunExitMessage;

    private String testRunStderr;

    private String testRunStdout;

    private UnitTestResultDTO testRunUnitTestResult;
}
