package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CreateUserDTO;
import com.example.rpl.RPL.controller.dto.JwtResponseDTO;
import com.example.rpl.RPL.controller.dto.LoginDTO;
import com.example.rpl.RPL.controller.dto.UserDTO;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.security.JwtTokenProvider;
import com.example.rpl.RPL.service.AuthenticationService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;


    @Autowired
    public AuthenticationController(
        AuthenticationService authenticationService,
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(value = "/api/auth/signup")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid final CreateUserDTO createUserDTO) {

        User user = authenticationService
            .createUser(createUserDTO.getName(), createUserDTO.getSurname(),
                createUserDTO.getStudentId(), createUserDTO.getUsername(), createUserDTO.getEmail(),
                createUserDTO.getPassword(), createUserDTO.getUniversity(),
                createUserDTO.getDegree());

        return new ResponseEntity<>(UserDTO.fromEntity(user), HttpStatus.CREATED);
    }


    @PostMapping("/api/auth/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        log.info("[process:login] User {} Logged in", loginDto.getUsernameOrEmail());
        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }
}
