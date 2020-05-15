package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ForgotPasswordRequestDTO {

    @NotNull
    private String email;

    @JsonCreator
    public ForgotPasswordRequestDTO(
        @JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

}