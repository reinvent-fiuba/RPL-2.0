package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.*;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.ValidationToken;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.JwtTokenProvider;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UsersController {

    private final AuthenticationService authenticationService;

    @Autowired
    public UsersController(
        AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasAuthority('superadmin')")
    @GetMapping(value = "/api/users")
    public ResponseEntity<List<UserResponseDTO>> findUsers(
            @RequestParam(required = false) String query) {

        return new ResponseEntity<>(
                authenticationService.findUsers(query)
                        .stream()
                        .map(UserResponseDTO::fromEntity)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
