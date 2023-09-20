package com.example.rpl.RPL.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ForgotPasswordRequestDTO {

    @NotNull
    private String email;
}
