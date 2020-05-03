package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatchCourseUserRequestDTO {

    private Boolean accepted;

    private String role;
}
