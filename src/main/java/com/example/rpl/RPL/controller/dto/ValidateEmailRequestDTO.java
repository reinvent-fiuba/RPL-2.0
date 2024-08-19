package com.example.rpl.RPL.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ValidateEmailRequestDTO {

    @NotNull
    private String validateEmailToken;
}
