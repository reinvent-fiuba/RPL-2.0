package com.example.rpl.RPL.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LoginRequestDTO {

    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String password;
}
