package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Value
public class CreateCourseRequestDTO {

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

    @NotNull
    private Long courseAdminId;

    private String description;
}
