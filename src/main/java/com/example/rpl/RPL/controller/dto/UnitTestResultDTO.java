package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonDeserialize
@NoArgsConstructor
public class UnitTestResultDTO {

    @JsonProperty("passed")
    Integer passed;

    @JsonProperty("failed")
    Integer failed;

    @JsonProperty("errored")
    Integer errored;

    @JsonProperty("tests")
    List<TestSuitDTO> tests;

    @JsonCreator
    public UnitTestResultDTO(
        @JsonProperty(value = "passed", required = true) Integer passed,
        @JsonProperty(value = "failed", required = true) Integer failed,
        @JsonProperty(value = "errored", required = true) Integer errored,
        @JsonProperty(value = "tests", required = true) List<TestSuitDTO> tests) {
        this.passed = passed;
        this.failed = failed;
        this.errored = errored;
        this.tests = tests;
    }


    @Getter
    @Builder
    @JsonDeserialize
    public static class TestSuitDTO {

        @JsonProperty("name")
        String name;

        @JsonProperty("assertions")
        Integer assertions;

        @JsonProperty("status")
        String status;

        @JsonProperty("messages")
        String messages;

        @JsonCreator
        public TestSuitDTO(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "assertions", required = true) Integer assertions,
            @JsonProperty(value = "status", required = true) String status,
            @JsonProperty(value = "messages") String messages) {
            this.name = name;
            this.assertions = assertions;
            this.status = status;
            this.messages = messages;
        }
    }
}















