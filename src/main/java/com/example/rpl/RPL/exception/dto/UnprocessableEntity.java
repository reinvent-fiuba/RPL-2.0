package com.example.rpl.RPL.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

/**
 * DTO used to respond when there are validation errors (Bean Validation). This object
 * will be returned as a JSON object.
 */
public class UnprocessableEntity extends ErrorResponse {

    @JsonProperty("cause")
    private final Set<ValidationError> validationErrors = new LinkedHashSet<>();

    public UnprocessableEntity() {
        super(UNPROCESSABLE_ENTITY, "validation_error", "Invalid entity.");
    }

    public void addValidationError(String property, String message) {
        this.validationErrors.add(new ValidationError(property, message));
    }

    @JsonIgnore
    public String getValidationErrorsMessage() {
        return validationErrors.stream().map(v -> "[property:" + v.property + "][message:" + v.message + "]").reduce("", String::concat);
    }

    @JsonIgnore
    @Override
    public String getCause() {
        return super.getCause();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class ValidationError {

        private String property;
        private String message;
    }
}
