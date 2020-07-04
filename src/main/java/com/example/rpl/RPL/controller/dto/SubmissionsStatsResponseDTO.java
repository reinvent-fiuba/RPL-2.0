package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.stats.SubmissionsStat;
import com.example.rpl.RPL.model.stats.SubmissionsStats;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class SubmissionsStatsResponseDTO {

    private List<SubmissionsStatResponseDTO> submissionsStats;

    List<Map<String, String>> metadata;

    public static SubmissionsStatsResponseDTO fromEntity(SubmissionsStats submissionsStats) {
        return SubmissionsStatsResponseDTO.builder()
            .submissionsStats(submissionsStats
                            .getSubmissionsStats()
                            .stream()
                            .map(SubmissionsStatResponseDTO::fromEntity).collect(Collectors.toList())
            )
            .metadata(submissionsStats.getMetadata())
            .build();
    }
}
