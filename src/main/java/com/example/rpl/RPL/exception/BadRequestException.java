package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Bad Request exception.
 */
public class BadRequestException extends BaseAPIException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, String error) {
        super(message, HttpStatus.BAD_REQUEST, error);
    }

    public BadRequestException(String message, String error, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, error);
    }
}
