package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitiesStats;
import com.example.rpl.RPL.model.ActivitySubmissionStats;
import com.example.rpl.RPL.model.SubmissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ActivitiesStatsResponseDTO {

    private Integer total;

    private Map<String, Long> countByStatus;

    private Map<String, Long> score;

    public static ActivitiesStatsResponseDTO fromEntity(ActivitiesStats activitiesStats) {
        return ActivitiesStatsResponseDTO.builder()
            .total(activitiesStats.getTotal())
            .countByStatus(activitiesStats.getCountByStatus())
            .score(activitiesStats.getScore())
            .build();
    }
}
