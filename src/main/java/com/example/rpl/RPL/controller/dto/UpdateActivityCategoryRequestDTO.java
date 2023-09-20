package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Value
public class UpdateActivityCategoryRequestDTO {

    private String name;

    private String description;

    private Boolean active;

}
