package com.example.rpl.RPL.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class CreateUserRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String name;

    private String surname;

    private String studentId;

    private String degree;

    private String university;
}
