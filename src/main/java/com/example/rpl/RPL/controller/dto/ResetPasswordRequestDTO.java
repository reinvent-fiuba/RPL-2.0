package com.example.rpl.RPL.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDTO {

    @NotNull
    private String passwordToken;

    @NotNull
    private String newPassword;

    @JsonCreator
    public ResetPasswordRequestDTO(
        @JsonProperty(value = "password_token", required = true) String passwordToken,
        @JsonProperty(value = "new_password", required = true) String newPassword) {
        this.passwordToken = passwordToken;
        this.newPassword = newPassword;
    }

}