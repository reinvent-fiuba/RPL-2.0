package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.*;
import com.example.rpl.RPL.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(
        UsersService usersService
    ) {
        this.usersService = usersService;
    }

    @PreAuthorize("hasAuthority('superadmin')")
    @GetMapping(value = "/api/users")
    public ResponseEntity<List<UserResponseDTO>> findUsers(
            @RequestParam(required = false) String query) {

        return new ResponseEntity<>(
                usersService.findUsers(query)
                        .stream()
                        .map(UserResponseDTO::fromEntity)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
