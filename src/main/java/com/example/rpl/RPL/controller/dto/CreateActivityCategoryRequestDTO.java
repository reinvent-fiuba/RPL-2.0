package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CreateActivityCategoryRequestDTO {

    @NotNull
    private String name;

    private String description;

}
