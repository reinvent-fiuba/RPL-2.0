package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.AllArgsConstructor;
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

    @JsonProperty("success")
    Boolean success;

    @JsonProperty("tests")
    List<TestSuitDTO> tests;

    @JsonCreator
    public UnitTestResultDTO(
        @JsonProperty(value = "success", required = true) Boolean success,
        @JsonProperty(value = "tests", required = true) List<TestSuitDTO> tests) {
        this.success = success;
        this.tests = tests;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @JsonDeserialize
    public static class TestSuitDTO {

        @JsonProperty("name")
        String name;

        @JsonProperty("success")
        Boolean success;

        @JsonProperty("description")
        String description;

        @JsonCreator
        public TestSuitDTO(
            @JsonProperty(value = "success", required = true) Boolean success,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "description", required = true) String description) {
            this.success = success;
            this.name = name;
            this.description = description;
        }
    }
}















