package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseUserResponseDTO {

    private Long id;

    private Long courseUserId;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private String email;

    private Boolean emailValidated;

    private String university;

    private String degree;

    private String role;

    private Boolean accepted;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseUserResponseDTO fromEntity(CourseUser courseUser) {
        User user = courseUser.getUser();
        Role role = courseUser.getRole();
        return CourseUserResponseDTO.builder()
            .id(user.getId())
            .courseUserId(courseUser.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .studentId(user.getStudentId())
            .username(user.getUsername())
            .email(user.getEmail())
            .emailValidated(user.getEmailValidated())
            .university(user.getUniversity())
            .degree(user.getDegree())
            .role(role.getName())
            .accepted(courseUser.getAccepted())
            .dateCreated(user.getDateCreated())
            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
