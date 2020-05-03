package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateSubmissionStatusRequestDTO {

    private final SubmissionStatus status;

    @JsonCreator
    public UpdateSubmissionStatusRequestDTO(
        @JsonProperty(value = "status", required = true) String status) {
        this.status = SubmissionStatus.valueOf(status);

    }
}
