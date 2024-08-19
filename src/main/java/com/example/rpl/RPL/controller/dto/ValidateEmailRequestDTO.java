package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ValidateEmailRequestDTO {

    @NotNull
    private String validateEmailToken;

    @JsonCreator
    public ValidateEmailRequestDTO(@JsonProperty("validateEmailToken") String validateEmailToken) {
        this.validateEmailToken = validateEmailToken;
    }
}
