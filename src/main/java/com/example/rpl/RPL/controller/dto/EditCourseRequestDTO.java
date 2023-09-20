package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Value
public class EditCourseRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String university;

    @NotNull
    private String universityCourseId;

    @NotNull
    private String semester;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate semesterStartDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate semesterEndDate;

    private String description;

    private String imgUri;
}
