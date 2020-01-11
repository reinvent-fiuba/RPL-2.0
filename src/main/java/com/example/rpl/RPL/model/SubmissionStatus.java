package com.example.rpl.RPL.model;

public enum SubmissionStatus {
    PENDING, ENQUEUED, BUILDING, BUILDING_ERROR, RUNTIME_ERROR, FAILURE, SUCCESS;


    public static SubmissionStatus getStatusIfError(String stage) {
        if ("BUILD".equals(stage)) {
            return BUILDING_ERROR;
        }
        return RUNTIME_ERROR;
    }
}
