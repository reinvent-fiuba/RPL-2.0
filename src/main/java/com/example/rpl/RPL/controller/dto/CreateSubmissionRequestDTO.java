package com.example.rpl.RPL.controller.dto;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
class CreateSubmissionRequestDTO {

    @Valid
    private List<Map<String, String>> files;

}
