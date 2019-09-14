package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Forbidden exception.
 */
public class ForbiddenException extends BaseAPIException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, String error) {
        super(message, HttpStatus.FORBIDDEN, error);
    }
}
