package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class LoginRequestDTO {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}