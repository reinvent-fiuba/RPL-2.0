package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class EditUserRequestDTO {

    private String email;

    private String name;

    private String surname;

    private String studentId;

    private String degree;

    private String university;

    private String imgUri;
}
