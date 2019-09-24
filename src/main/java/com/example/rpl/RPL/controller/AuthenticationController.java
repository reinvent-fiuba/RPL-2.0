package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CreateUserDTO;
import com.example.rpl.RPL.controller.dto.UserDTO;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.service.AuthenticationService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(
        AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/api/auth/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid final CreateUserDTO createUserDTO) {

        User user = authenticationService
            .createUser(createUserDTO.getName(), createUserDTO.getSurname(),
                createUserDTO.getStudentId(), createUserDTO.getUsername(), createUserDTO.getEmail(),
                createUserDTO.getPassword(), createUserDTO.getUniversity(),
                createUserDTO.getDegree());

        return new ResponseEntity<>(UserDTO.fromEntity(user), HttpStatus.CREATED);
    }
}
