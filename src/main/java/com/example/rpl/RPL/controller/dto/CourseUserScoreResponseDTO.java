package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class CourseUserScoreResponseDTO {

    private Long id;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private String email;

    private Long score;

    private Long activitiesCount;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseUserScoreResponseDTO fromEntity(CourseUser courseUser, Long score, Long activitiesCount) {
        User user = courseUser.getUser();
        Role role = courseUser.getRole();
        return CourseUserScoreResponseDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .studentId(user.getStudentId())
            .username(user.getUsername())
            .email(user.getEmail())
            .score(score)
            .activitiesCount(activitiesCount)
            .dateCreated(user.getDateCreated())
            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
