package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Value
public class UpdateActivityRequestDTO {

    private Long activityCategoryId;

    private String name;

    private String description;

    private String language;

    private String compilationFlags;

    private Boolean active;

    private Long points;
}
