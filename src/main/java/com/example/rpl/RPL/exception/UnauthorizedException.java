package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * Unauthorized exception.
 */
class UnauthorizedException extends BaseAPIException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, String error) {
        super(message, HttpStatus.UNAUTHORIZED, error);
    }
}
