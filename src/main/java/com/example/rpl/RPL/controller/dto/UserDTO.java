package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.User;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private String email;

    private Boolean emailValidated;

    private String university;

    private String degree;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
            .name(user.getName())
            .surname(user.getSurname())
            .studentId(user.getStudentId())
            .username(user.getName())
            .email(user.getEmail())
            .emailValidated(user.getEmailValidated())
            .university(user.getUniversity())
            .degree(user.getDegree())
            .dateCreated(user.getDateCreated())
            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
