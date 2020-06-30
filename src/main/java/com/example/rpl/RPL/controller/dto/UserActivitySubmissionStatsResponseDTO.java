package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivityStats;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.UserActivitySubmissionStats;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserActivitySubmissionStatsResponseDTO {

    private Long id;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private Long totalSubmissions;

    private Long successSubmissions;

    public static UserActivitySubmissionStatsResponseDTO fromEntity(UserActivitySubmissionStats userActivitySubmissionStats) {
        User user = userActivitySubmissionStats.getUser();
        return UserActivitySubmissionStatsResponseDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .username(user.getUsername())
            .totalSubmissions(userActivitySubmissionStats.getTotalSubmissions())
            .successSubmissions(userActivitySubmissionStats.getSuccessSubmissions())
            .build();
    }
}
