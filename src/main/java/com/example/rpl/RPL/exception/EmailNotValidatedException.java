package com.example.rpl.RPL.exception;

import org.springframework.http.HttpStatus;

/**
 * EmailNotValidated exception.
 */
public class EmailNotValidatedException extends BaseAPIException {

    public EmailNotValidatedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "email_not_validated_error");
    }

    public EmailNotValidatedException(String message, String error) {
        super(message, HttpStatus.UNAUTHORIZED, error);
    }
}
