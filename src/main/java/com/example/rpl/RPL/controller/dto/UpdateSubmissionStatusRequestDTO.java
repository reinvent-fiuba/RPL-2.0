package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class UpdateSubmissionStatusRequestDTO {

    private final SubmissionStatus status;
}
