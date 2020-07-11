package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.CourseUserScore;
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

    private String imgUri;

    private Long score;

    private Long activitiesCount;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseUserScoreResponseDTO fromEntity(CourseUserScore courseUserScore) {
        User user = courseUserScore.getCourseUser().getUser();
        return CourseUserScoreResponseDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .studentId(user.getStudentId())
            .username(user.getUsername())
            .imgUri(user.getImgUri())
            .email(user.getEmail())
            .score(courseUserScore.getScore())
            .activitiesCount(courseUserScore.getActivitiesCount())
            .dateCreated(user.getDateCreated())
            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
