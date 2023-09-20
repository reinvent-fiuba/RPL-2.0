package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.*;
import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.University;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.ValidationToken;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.JwtTokenProvider;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

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

        authenticationService.sendValidateEmailToken(user);

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

    @PatchMapping("/api/auth/profile")
    public ResponseEntity<UserResponseDTO> updateUser(
            @CurrentUser UserPrincipal currentUser,
            @RequestBody(required=false) EditUserRequestDTO editUserRequestDTO) {

        User user = authenticationService.updateUser(
                currentUser.getId(),
                editUserRequestDTO.getName(),
                editUserRequestDTO.getSurname(),
                editUserRequestDTO.getStudentId(),
                editUserRequestDTO.getEmail(),
                editUserRequestDTO.getUniversity(),
                editUserRequestDTO.getDegree(),
                editUserRequestDTO.getImgUri()
        );

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    /**
     *
     */
    @PostMapping("/api/auth/forgotPassword")
    public ResponseEntity<ForgotPasswordRequestDTO> forgotPassword(
        @Valid @RequestBody final ForgotPasswordRequestDTO resetPasswordDTO) {

        authenticationService.sendResetPasswordToken(resetPasswordDTO.getEmail());

        return new ResponseEntity<>(resetPasswordDTO, HttpStatus.OK);
    }

    /**
     *
     */
    @PostMapping("/api/auth/resetPassword")
    public ResponseEntity<UserResponseDTO> resetPassword(
        @Valid @RequestBody final ResetPasswordRequestDTO resetPasswordDTO) {

        ValidationToken token = authenticationService
            .validateToken(resetPasswordDTO.getPasswordToken());

        User user = authenticationService.resetPassword(token, resetPasswordDTO.getNewPassword());

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    /**
     *
     */
    @PostMapping("/api/auth/validateEmail")
    public ResponseEntity<UserResponseDTO> validateEmail(
        @Valid @RequestBody final ValidateEmailRequestDTO validateEmailDTO) {

        ValidationToken token = authenticationService
            .validateToken(validateEmailDTO.getValidateEmailToken());

        User user = authenticationService.validateEmail(token.getUser());

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    /**
     *
     */
    @PostMapping("/api/auth/resendValidationEmail")
    public ResponseEntity<UserResponseDTO> resendValidationEmail(
        @Valid @RequestBody final ResendValidationEmailRequestDTO resendValidationEmailRequestDTO) {

        User user = authenticationService
            .getUserByUsernameOrEmail(resendValidationEmailRequestDTO.getUsernameOrEmail());

        authenticationService.sendValidateEmailToken(user);

        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    @GetMapping("/api/auth/roles")
    public ResponseEntity<List<RoleResponseDTO>> getRoles() {
        List<Role> roles = authenticationService.getRoles();
        return new ResponseEntity<>(roles.stream()
                .map(RoleResponseDTO::fromEntity)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/api/auth/universities")
    public ResponseEntity<List<UniversityResponseDTO>> getUniversities() {
        return new ResponseEntity<>(Arrays.stream(University.values()).
                map(university -> UniversityResponseDTO.fromEntity(university))
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
