package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class CreateActivityRequestDTO {

    @NotNull
    private Long activityCategoryId;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String language;

    @NotNull
    private String initialCode;

    private Long points;
}
