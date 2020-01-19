package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CreateActivityRequestDTO {

    @NotNull
    private Long activityCategoryId;

    @NotNull
    private String name;

    @NotNull
    private String language;

    private String description;
}
