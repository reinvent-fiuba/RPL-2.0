package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.CourseResponseDTO;
import com.example.rpl.RPL.controller.dto.CourseUserResponseDTO;
import com.example.rpl.RPL.controller.dto.CourseUserScoreResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateCourseRequestDTO;
import com.example.rpl.RPL.controller.dto.EditCourseRequestDTO;
import com.example.rpl.RPL.controller.dto.PatchCourseUserRequestDTO;
import com.example.rpl.RPL.controller.dto.RoleResponseDTO;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.CourseUserScoreInterface;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.CoursesService;
import com.example.rpl.RPL.service.SubmissionService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CoursesController {

    private final CoursesService coursesService;
    private final SubmissionService submissionService;
    private final ActivitiesService activitiesService;

    @Autowired
    public CoursesController(
        CoursesService coursesService,
        SubmissionService submissionService,
        ActivitiesService activitiesService) {
        this.coursesService = coursesService;
        this.submissionService = submissionService;
        this.activitiesService = activitiesService;
    }

    @GetMapping(value = "/api/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
        @CurrentUser UserPrincipal currentUser) {

        List<Triplet<Course, Boolean, Boolean>> courses = coursesService
            .getAllCourses(currentUser.getId());

        return new ResponseEntity<>(
            courses.stream()
                .map(triplet ->
                    // If the user is not enrolled and not accepted we don't return user info
                    (!triplet.getValue1() && !triplet.getValue2()) ?
                        CourseResponseDTO.fromEntity(triplet.getValue0()) :
                        CourseResponseDTO.fromEntity(triplet.getValue0(), triplet.getValue1(),
                            triplet.getValue2())
                )
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('superadmin')")
    @PostMapping(value = "/api/courses")
    public ResponseEntity<CourseResponseDTO> createCourse(@CurrentUser UserPrincipal currentUser,
        @RequestBody @Valid CreateCourseRequestDTO createCourseRequestDTO) {

        Course course = createCourseRequestDTO.getId() == null ? coursesService.createCourse(
            createCourseRequestDTO.getName(),
            createCourseRequestDTO.getUniversity(),
            createCourseRequestDTO.getUniversityCourseId(),
            createCourseRequestDTO.getDescription(),
            true,
            createCourseRequestDTO.getSemester(),
            createCourseRequestDTO.getSemesterStartDate(),
            createCourseRequestDTO.getSemesterEndDate(),
            createCourseRequestDTO.getImgUri(),
            createCourseRequestDTO.getCourseAdminId()
        ) : coursesService.cloneCourse(
            createCourseRequestDTO.getId(),
            createCourseRequestDTO.getName(),
            createCourseRequestDTO.getUniversity(),
            createCourseRequestDTO.getUniversityCourseId(),
            createCourseRequestDTO.getDescription(),
            true,
            createCourseRequestDTO.getSemester(),
            createCourseRequestDTO.getSemesterStartDate(),
            createCourseRequestDTO.getSemesterEndDate(),
            createCourseRequestDTO.getImgUri(),
            createCourseRequestDTO.getCourseAdminId()
        );

        return new ResponseEntity<>(CourseResponseDTO.fromEntity(course), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('course_edit')")
    @PutMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<CourseResponseDTO> editCourse(@CurrentUser UserPrincipal currentUser,
        @RequestBody @Valid EditCourseRequestDTO editCourseRequestDTO,
        @PathVariable Long courseId) {

        Course course = coursesService.editCourse(
            courseId,
            editCourseRequestDTO.getName(),
            editCourseRequestDTO.getUniversity(),
            editCourseRequestDTO.getUniversityCourseId(),
            editCourseRequestDTO.getDescription(),
            true,
            editCourseRequestDTO.getSemester(),
            editCourseRequestDTO.getSemesterStartDate(),
            editCourseRequestDTO.getSemesterEndDate(),
            editCourseRequestDTO.getImgUri()
        );

        return new ResponseEntity<>(CourseResponseDTO.fromEntity(course), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('course_view')")
    @GetMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourseDetails(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {

        Course course = coursesService.getCourse(courseId);

        return new ResponseEntity<>(CourseResponseDTO.fromEntity(course), HttpStatus.OK);
    }

    @GetMapping(value = "/api/courses/{courseId}/permissions")
    public ResponseEntity<List<String>> getPermissions(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {

        List<String> permissions = coursesService.getPermissions(courseId, currentUser.getId());

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_view')")
    @GetMapping(value = "/api/courses/{courseId}/users")
    public ResponseEntity<List<CourseUserResponseDTO>> getCourseUsers(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @RequestParam(required = false) String roleName) {

        List<CourseUser> courseUsers = coursesService.getAllUsers(courseId, roleName);

        return new ResponseEntity<>(
            courseUsers.stream()
                .map(CourseUserResponseDTO::fromEntity)
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_view')")
    @GetMapping(value = "/api/courses/{courseId}/scoreboard")
    public ResponseEntity<List<CourseUserScoreResponseDTO>> getCourseScoreboard(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {

        List<CourseUserScoreInterface> scoreboard = coursesService.getScoreboard(courseId);

        return new ResponseEntity<>(
            scoreboard.stream()
                .map(CourseUserScoreResponseDTO::fromEntity)
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_manage')")
    @PatchMapping(value = "/api/courses/{courseId}/users/{userId}")
    public ResponseEntity<CourseUserResponseDTO> updateCourseUser(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long userId,
        @RequestBody @Valid PatchCourseUserRequestDTO patchCourseUserRequestDTO) {

        CourseUser courseUser = coursesService.updateCourseUser(
            courseId,
            userId,
            patchCourseUserRequestDTO.getAccepted(),
            patchCourseUserRequestDTO.getRole()
        );

        return new ResponseEntity<>(
            CourseUserResponseDTO.fromEntity(courseUser),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_manage')")
    @DeleteMapping(value = "/api/courses/{courseId}/users/{userId}")
    public ResponseEntity deleteCourseUser(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long userId) {

        coursesService.deleteCourseUser(userId, courseId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
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
    public ResponseEntity unenrollInCourse(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {

        coursesService.deleteCourseUser(currentUser.getId(), courseId);

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
