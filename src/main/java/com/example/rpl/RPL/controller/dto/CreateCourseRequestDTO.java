package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
