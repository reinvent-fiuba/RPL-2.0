package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public class RoleResponseDTO {

    private Long id;

    private String name;

    private List<String> permissions;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static RoleResponseDTO fromEntity(Role role) {
        return RoleResponseDTO.builder()
            .id(role.getId())
            .name(role.getName())
            .permissions(role.getPermissions())
            .dateCreated(role.getDateCreated())
            .lastUpdated(role.getLastUpdated())
            .build();
    }
}
