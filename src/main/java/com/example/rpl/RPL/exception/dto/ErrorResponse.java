package com.example.rpl.RPL.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Locale;
import org.springframework.http.HttpStatus;

/**
 * Base DTO used to respond when an exception happen.
 */
public class ErrorResponse {

    @JsonIgnore
    private HttpStatus status;
    private String message;
    private final String error;
    @JsonIgnore
    private Throwable cause;

    private ErrorResponse() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = "Internal error. Something did not work well.";
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase(Locale.ENGLISH);
        this.cause = null;
    }

    private ErrorResponse(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
    }

    public ErrorResponse(HttpStatus status, String error, String message) {
        this(status, error);
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String error, String message, Throwable cause) {
        this(status, error, message);
        this.cause = cause;
    }

    public ErrorResponse(Throwable cause) {
        this();
        this.cause = cause;
    }

    @JsonProperty
    public int getStatus() {
        return status.value();
    }

    @JsonIgnore
    public HttpStatus getStatusObj() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    @JsonProperty
    @JsonDeserialize(using = HttpStatusDeserializer.class)
    public void setHttpStatus(HttpStatus httpStatus) {
        this.status = httpStatus;
    }

    @JsonProperty
    String getCause() {
        if (cause != null) {
            return cause.getLocalizedMessage();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}