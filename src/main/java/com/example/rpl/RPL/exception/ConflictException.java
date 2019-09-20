package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Conflict exception.
 */
public class ConflictException extends BaseAPIException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, String error) {
        super(message, HttpStatus.CONFLICT, error);
    }
}
