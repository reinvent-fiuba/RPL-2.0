package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class PatchCourseUserRequestDTO {

    private Boolean accepted;

    private String role;
}
