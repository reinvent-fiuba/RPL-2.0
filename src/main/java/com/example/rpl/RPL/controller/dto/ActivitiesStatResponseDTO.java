package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.stats.ActivitiesStat;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ActivitiesStatResponseDTO {

    private Long started;

    private Long notStarted;

    private Long solved;

    private Long obtainedPoints;

    private Long totalPoints;


    public static ActivitiesStatResponseDTO fromEntity(ActivitiesStat activitiesStat) {
        return ActivitiesStatResponseDTO.builder()
            .started(activitiesStat.getStarted())
            .notStarted(activitiesStat.getNotStarted())
            .solved(activitiesStat.getSolved())
            .obtainedPoints(activitiesStat.getObtainedPoints())
            .totalPoints(activitiesStat.getTotalPoints())
            .build();
    }
}
