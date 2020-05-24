package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.*;
import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.CoursesService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.validation.Valid;

import com.example.rpl.RPL.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PreAuthorize("hasAuthority('course_view')")
    @GetMapping(value = "/api/courses/{courseId}")
    public ResponseEntity<UserPrincipal> getCourseDetails(@CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {
        log.error("COURSE ID: {}", courseId);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('course_view')")
    @GetMapping(value = "/api/courses/{courseId}/permissions")
    public ResponseEntity<List<String>> getCourseUsers(@CurrentUser UserPrincipal currentUser,
                                                                      @PathVariable Long courseId) {

        List<String> permissions = coursesService.getPermissions(courseId, currentUser.getId());

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_view')")
    @GetMapping(value = "/api/courses/{courseId}/users")
    public ResponseEntity<List<CourseUserResponseDTO>> getCourseUsers(@CurrentUser UserPrincipal currentUser,
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
    public ResponseEntity<List<CourseUserScoreResponseDTO>> getCourseScoreboard(@CurrentUser UserPrincipal currentUser,
                                                                                @PathVariable Long courseId) {


        List<Activity> courseActivities = activitiesService.getAllActivitiesByCourse(courseId);
        List<CourseUserScoreResponseDTO> scoreboard =
                coursesService.getAllUsers(courseId, "student").stream().map(courseUser -> {
                    LongSummaryStatistics userActivityPoints = submissionService
                            .getAllSubmissionsByUserAndActivities(courseUser.getUser(), courseActivities)
                            .stream()
                            .filter(activitySubmission -> activitySubmission.getStatus() == SubmissionStatus.SUCCESS)
                            .map(activitySubmission -> activitySubmission.getActivity())
                            .distinct()
                            .mapToLong(activity -> activity.getPoints())
                            .summaryStatistics();

                    Long score = userActivityPoints.getSum();
                    Long activityCount = userActivityPoints.getCount();

                    return CourseUserScoreResponseDTO.fromEntity(courseUser, score, activityCount);
                }).collect(Collectors.toList());


        return new ResponseEntity<>(
                scoreboard,
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user_manage')")
    @PatchMapping(value = "/api/courses/{courseId}/users/{userId}")
    public ResponseEntity<CourseUserResponseDTO> updateCourseUser(@CurrentUser UserPrincipal currentUser,
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
