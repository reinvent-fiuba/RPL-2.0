package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.User;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {

    private Long id;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private String email;

    private Boolean emailValidated;

    private Boolean isAdmin;

    private String university;

    private String degree;

    private String imgUri;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .studentId(user.getStudentId())
            .username(user.getUsername())
            .email(user.getEmail())
            .emailValidated(user.getEmailValidated())
            .isAdmin(user.getIsAdmin())
            .university(user.getUniversity())
            .degree(user.getDegree())
            .imgUri(user.getImgUri())
            .dateCreated(user.getDateCreated())
            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
