package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CreateCourseRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String university;

    @NotNull
    private String universityCourseId;

    @NotNull
    private String semester;

    private String description;
}
