package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CourseResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateCourseRequestDTO;
import com.example.rpl.RPL.controller.dto.RoleResponseDTO;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.CoursesService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CoursesController {

    private CoursesService coursesService;

    @Autowired
    public CoursesController(
        CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @PostMapping(value = "/api/courses")
    public ResponseEntity<CourseResponseDTO> createCourse(@CurrentUser UserPrincipal currentUser,
        @RequestBody @Valid CreateCourseRequestDTO createCourseRequestDTO) {

        Course course = coursesService.createCourse(
            createCourseRequestDTO.getName(),
            createCourseRequestDTO.getUniversityCourseId(),
            createCourseRequestDTO.getDescription(),
            true,
            createCourseRequestDTO.getSemester(),
            null,
            currentUser.getUser()
        );

        return new ResponseEntity<>(CourseResponseDTO.fromEntity(course), HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCourses() {

        List<Course> courses = coursesService.getAllCourses();

        return new ResponseEntity<>(
            courses.stream()
                .map(CourseResponseDTO::fromEntity)
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('course_create')")
    @GetMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<UserPrincipal> getCourseDetails(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {
        log.error("COURSE ID: {}", courseId);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PostMapping(value = "/api/courses/{courseId}/enroll")
    public ResponseEntity<RoleResponseDTO> enrollInCourse(@CurrentUser UserPrincipal currentUser,
                                                        @PathVariable Long courseId) {

        CourseUser courseUser = coursesService.enrollInCourse(currentUser.getId(), courseId);

        return new ResponseEntity<>(
            RoleResponseDTO.fromEntity(courseUser.getRole()),
            HttpStatus.OK);
    }

    @PostMapping(value = "/api/courses/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollInCourse(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long courseId) {

        coursesService.unenrollInCourse(currentUser.getId(), courseId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/api/users/{userId}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesOfUser(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long userId) {
        if (!currentUser.getId().equals(userId)) {
            return new ResponseEntity<>(
                Collections.emptyList(),
                HttpStatus.OK
            );
        }

        List<Course> courses = coursesService.getAllCoursesByUser(userId);

        return new ResponseEntity<>(
            courses.stream()
                .map(CourseResponseDTO::fromEntity)
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }
}
