package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CourseDTO;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.CoursesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CoursesController {

    private CoursesService coursesService;

    @Autowired
    public CoursesController(
            CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping(value = "/api/courses")
    public ResponseEntity<List<CourseDTO>> getCourses(@CurrentUser UserPrincipal currentUser) {

        List<Course> courses = coursesService.getAllCourses();

        return new ResponseEntity<>(
                courses.stream()
                        .map(courseSemester -> CourseDTO.fromEntity(courseSemester))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('course_create')")
    @GetMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<UserPrincipal> getCourseDetails(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long courseId) {
        log.error("POLL ID: {}", courseId);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping(value="/api/users/{userId}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesOfUser(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable Long userId) {
        if (currentUser.getId() != userId) {
            return new ResponseEntity<>(
                Collections.emptyList(),
                HttpStatus.OK
            );
        }

        List<Course> courses = coursesService.getAllCoursesByUser(userId);

        return new ResponseEntity<>(
            courses.stream()
                .map(courseSemester -> CourseDTO.fromEntity(courseSemester))
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }
}
