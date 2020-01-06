package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CreateUserRequestDTO;
import com.example.rpl.RPL.controller.dto.JwtResponseDTO;
import com.example.rpl.RPL.controller.dto.LoginRequestDTO;
import com.example.rpl.RPL.controller.dto.UserResponseDTO;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.JwtTokenProvider;
import com.example.rpl.RPL.security.UserPrincipal;
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
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<UserResponseDTO> registerUser(
        @RequestBody @Valid final CreateUserRequestDTO createUserRequestDTO) {

        User user = authenticationService
            .createUser(createUserRequestDTO.getName(), createUserRequestDTO.getSurname(),
                createUserRequestDTO.getStudentId(), createUserRequestDTO.getUsername(),
                createUserRequestDTO.getEmail(),
                createUserRequestDTO.getPassword(), createUserRequestDTO.getUniversity(),
                createUserRequestDTO.getDegree());

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.CREATED);
    }


    /**
     * If authentication fails, throws BadCredentialsException.
     *
     * @return JwtResponseDTO with JWT token.
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(
        @Valid @RequestBody LoginRequestDTO loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsernameOrEmail(),
                loginRequestDto.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        log.info("[process:login] User {} Logged in", loginRequestDto.getUsernameOrEmail());
        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }

    /**
     * If authentication fails, throws BadCredentialsException.
     *
     * @return JwtResponseDTO with JWT token.
     */
    @GetMapping("/api/auth/profile")
    public ResponseEntity<UserResponseDTO> getUser(@CurrentUser UserPrincipal currentUser) {

        User user = authenticationService.getUserById(currentUser.getId());

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }
}
