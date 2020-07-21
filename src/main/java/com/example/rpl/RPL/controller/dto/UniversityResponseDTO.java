package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.University;
import com.example.rpl.RPL.model.stats.ActivitiesStat;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Array;

@Getter
@Builder
public class UniversityResponseDTO {

    private Long id;

    private String name;

    private String[] degrees;

    public static UniversityResponseDTO fromEntity(University university) {
        return UniversityResponseDTO.builder()
            .id(university.getId())
            .name(university.toString())
            .degrees(university.getDegrees())
            .build();
    }
}
