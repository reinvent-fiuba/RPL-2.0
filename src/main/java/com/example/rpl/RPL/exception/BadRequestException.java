package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Bad Request exception.
 */
class BadRequestException extends BaseAPIException {

    BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    BadRequestException(String message, String error) {
        super(message, HttpStatus.BAD_REQUEST, error);
    }

    public BadRequestException(String message, String error, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, error);
    }
}
