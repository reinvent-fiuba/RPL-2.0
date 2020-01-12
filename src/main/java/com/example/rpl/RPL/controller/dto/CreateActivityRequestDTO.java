package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CreateActivityRequestDTO {

    private Long activityCategoryId;

    private String name;

    private String description;

    private String language;
}
