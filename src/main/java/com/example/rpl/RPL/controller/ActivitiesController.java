package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateActivityRequestDTO;
import com.example.rpl.RPL.controller.dto.UpdateActivityRequestDTO;
import com.example.rpl.RPL.controller.dto.UserActivityResponseDTO;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.service.TestService;
import com.example.rpl.RPL.utils.TarUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class ActivitiesController {

    private final ActivitiesService activitiesService;
    private final SubmissionService submissionService;
    private final TestService testService;

    @Autowired
    public ActivitiesController(
        ActivitiesService activitiesService,
        SubmissionService submissionService, TestService testService) {
        this.activitiesService = activitiesService;
        this.submissionService = submissionService;
        this.testService = testService;
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PostMapping(value = "/api/courses/{courseId}/activities")
    public ResponseEntity<ActivityResponseDTO> createActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @Valid CreateActivityRequestDTO createActivityRequestDTO,
        @RequestParam(value = "startingFile") MultipartFile[] startingFiles) {

        byte[] compressedStartingFilesBytes = TarUtils.compressToTarGz(startingFiles);

        Activity activity = activitiesService.createActivity(
            courseId,
            createActivityRequestDTO.getActivityCategoryId(),
            createActivityRequestDTO.getName(),
            createActivityRequestDTO.getDescription(),
            createActivityRequestDTO.getLanguage(),
            createActivityRequestDTO.getActive(),
            createActivityRequestDTO.getPoints(),
            createActivityRequestDTO.getCompilationFlags(),
            compressedStartingFilesBytes);

        return new ResponseEntity<>(
            ActivityResponseDTO.fromEntity(activity, null, new ArrayList<>()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PatchMapping(value = "/api/courses/{courseId}/activities/{activityId}")
    public ResponseEntity<ActivityResponseDTO> updateActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @Valid UpdateActivityRequestDTO updateActivityRequestDTO,
        @RequestParam(value = "startingFile") MultipartFile[] startingFiles) {

        Activity activity = activitiesService.getActivity(activityId);

        byte[] compressedStartingFilesBytes = null;

        if (startingFiles.length != 0) {
            compressedStartingFilesBytes = TarUtils.compressToTarGz(startingFiles);
        }

        activity = activitiesService.updateActivity(
            activity,
            updateActivityRequestDTO.getActivityCategoryId(),
            updateActivityRequestDTO.getName(),
            updateActivityRequestDTO.getDescription(),
            updateActivityRequestDTO.getLanguage(),
            updateActivityRequestDTO.getActive(),
            updateActivityRequestDTO.getPoints(),
            updateActivityRequestDTO.getCompilationFlags(),
            compressedStartingFilesBytes);

        //        GET UNIT TESTS
        UnitTest unitTest = testService.getUnitTests(activity.getId());

        //        GET IO TESTS
        List<IOTest> ioTests = testService.getAllIOTests(activity.getId());

        return new ResponseEntity<>(
            ActivityResponseDTO.fromEntity(activity, unitTest, ioTests), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_view')")
    @GetMapping(value = "/api/courses/{courseId}/activities")
    public ResponseEntity<List<UserActivityResponseDTO>> getActivities(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {
        log.debug("COURSE ID: {}", courseId);


        List<Activity> activities = currentUser.hasAuthority("activity_manage") ?
                activitiesService.search(courseId) :
                activitiesService.getAllActiveActivitiesByCourse(courseId);

        List<ActivitySubmission> submissions = submissionService
            .getAllSubmissionsByUserAndActivities(currentUser.getUser(), activities);

        Map<Activity, List<ActivitySubmission>> submissionsByActivity = submissions.stream()
            .collect(Collectors.groupingBy(ActivitySubmission::getActivity));

        return new ResponseEntity<>(
            activities.stream()
                .map(activity -> UserActivityResponseDTO
                    .fromEntityWithStatus(activity, submissionsByActivity))
                .collect(Collectors.toList()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_view')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}")
    public ResponseEntity<ActivityResponseDTO> getActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {
        log.debug("COURSE ID: {}", courseId);

        Activity activity = activitiesService.getActivity(activityId);

        //        GET UNIT TESTS
        UnitTest unitTest = testService.getUnitTests(activity.getId());

        //        GET IO TESTS
        List<IOTest> ioTests = testService.getAllIOTests(activity.getId());

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity, unitTest, ioTests),
            HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('activity_manage')")
    @DeleteMapping(value = "/api/courses/{courseId}/activities/{activityId}")
    public ResponseEntity<ActivityResponseDTO> deleteActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {
        log.debug("COURSE ID: {}", courseId);

        Activity activity = activitiesService.deleteActivity(activityId);

        return new ResponseEntity<>(
            ActivityResponseDTO.fromEntity(activity, null, null), HttpStatus.OK);
    }
}
