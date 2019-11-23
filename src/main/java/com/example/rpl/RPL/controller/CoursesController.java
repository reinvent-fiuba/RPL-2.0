package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CoursesController {

    @PreAuthorize("hasAuthority('course_create')")
    @GetMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<UserPrincipal> getCourseDetails(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {
        log.error("POLL ID: {}", courseId);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

}