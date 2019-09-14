package com.example.rpl.RPL.exception;

/**
 * Validation Request exception.
 */
public class ValidationException extends BadRequestException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String error) {
        super(message, error);
    }
}
