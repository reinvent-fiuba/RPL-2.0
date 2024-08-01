package com.example.rpl.RPL.controller.dto;

import jakarta.validation.constraints.NotNull;
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

    private String compilationFlags;

    private Boolean active;

    private Long points;
}
