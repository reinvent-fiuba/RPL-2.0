package com.example.rpl.RPL.exception;

/**
 * Entity already exists exception.
 */
public class EntityAlreadyExistsException extends BadRequestException {

    public EntityAlreadyExistsException() {
        super("Entity already exists.");
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, String error) {
        super(message, error);
    }

}
