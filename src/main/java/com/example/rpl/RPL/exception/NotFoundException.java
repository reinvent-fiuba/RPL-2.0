package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Not Found exception.
 */
public class NotFoundException extends BaseAPIException {

    public NotFoundException() {
        this("Entity not found.");
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message, String error) {
        super(message, HttpStatus.NOT_FOUND, error);
    }
}
