package com.example.rpl.RPL.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SubmissionStatus {
    PENDING("PENDING"), ENQUEUED("ENQUEUED"), PROCESSING("PROCESSING"), BUILD_ERROR("BUILD_ERROR"),
    RUNTIME_ERROR("RUNTIME_ERROR"), FAILURE("FAILURE"), SUCCESS("SUCCESS"), TIME_OUT("TIME_OUT");

    private String status;

    public String getStatus() {
        return status;
    }

    @JsonCreator
    private SubmissionStatus(String status) {
        this.status = status;
    }

    public static SubmissionStatus getStatusIfError(String stage) {
        if ("BUILD".equals(stage)) {
            return BUILD_ERROR;
        }
        return RUNTIME_ERROR;
    }
}
