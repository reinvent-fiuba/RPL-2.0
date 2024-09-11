package com.example.rpl.RPL.model;

public enum SubmissionStatus {
    PENDING, ENQUEUED, PROCESSING, BUILD_ERROR, RUNTIME_ERROR, FAILURE, SUCCESS, TIME_OUT;


    public static SubmissionStatus getStatusIfError(String stage) {
        if ("BUILD".equals(stage)) {
            return BUILD_ERROR;
        }
        return RUNTIME_ERROR;
    }
}
