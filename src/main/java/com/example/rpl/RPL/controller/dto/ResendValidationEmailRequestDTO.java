package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ResendValidationEmailRequestDTO {

    @NotNull
    private String usernameOrEmail;

    @JsonCreator
    public ResendValidationEmailRequestDTO(@JsonProperty("usernameOrEmail") String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
