package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

import java.util.Locale;


/**
 * Base exception used in backend.
 */
public class BaseAPIException extends RuntimeException {

    private final HttpStatus status;
    private final String error;

    public BaseAPIException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase(Locale.ENGLISH);
    }

    public BaseAPIException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase(Locale.ENGLISH);
    }

    public BaseAPIException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
        this.error = status.name().toLowerCase(Locale.ENGLISH);
    }

    public BaseAPIException(String message, Throwable cause, HttpStatus status, String error) {
        super(message, cause);
        this.status = status;
        this.error = error;
    }

    public BaseAPIException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.error = status.name().toLowerCase(Locale.ENGLISH);
    }

    public BaseAPIException(String message, HttpStatus status, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }
}
