package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class ActivitySubmissionStatsResponseDTO {

    private Integer total;

    private Map<SubmissionStatus, Long> countByStatus;

    public static ActivitySubmissionStatsResponseDTO fromEntity(ActivitySubmissionStats activitySubmissionStats) {
        return ActivitySubmissionStatsResponseDTO.builder()
            .total(activitySubmissionStats.getTotal())
            .countByStatus(activitySubmissionStats.getCountByStatus())
            .build();
    }
}
