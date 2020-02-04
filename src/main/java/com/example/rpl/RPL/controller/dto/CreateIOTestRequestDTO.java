package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateIOTestRequestDTO {

    private Long id;

    @NotNull
    private String textIn;

    @NotNull
    private String textOut;
}
