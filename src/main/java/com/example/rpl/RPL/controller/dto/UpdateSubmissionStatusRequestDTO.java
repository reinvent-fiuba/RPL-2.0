package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateSubmissionStatusRequestDTO {
    private SubmissionStatus status;

    // Default constructor
    public UpdateSubmissionStatusRequestDTO() {
    }

    // Constructor with @JsonCreator and @JsonProperty
    @JsonCreator
    public UpdateSubmissionStatusRequestDTO(@JsonProperty("status") SubmissionStatus status) {
        this.status = status;
    }

    // Getter and setter
    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}
