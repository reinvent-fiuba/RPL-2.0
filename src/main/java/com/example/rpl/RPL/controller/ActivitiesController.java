package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateActivityRequestDTO;
import com.example.rpl.RPL.controller.dto.UserActivityResponseDTO;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.utils.TarUtils;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class ActivitiesController {

    private ActivitiesService activitiesService;
    private SubmissionService submissionService;


    @Autowired
    public ActivitiesController(
        ActivitiesService activitiesService,
        SubmissionService submissionService) {
        this.activitiesService = activitiesService;
        this.submissionService = submissionService;
    }

    @PostMapping(value = "/api/courses/{courseId}/activities")
    public ResponseEntity<ActivityResponseDTO> createActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @Valid CreateActivityRequestDTO createActivityRequestDTO,
        @RequestParam(value = "supportingFile") MultipartFile[] supportingFiles) {

        byte[] compressedSupportingFilesBytes = TarUtils.compressToTarGz(supportingFiles);

        Activity activity = activitiesService.createActivity(
            courseId,
            createActivityRequestDTO.getActivityCategoryId(),
            createActivityRequestDTO.getName(),
            createActivityRequestDTO.getDescription(),
            createActivityRequestDTO.getLanguage(),
            true,
            createActivityRequestDTO.getInitialCode(),
            compressedSupportingFilesBytes);

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity), HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('course_create')")
    @GetMapping(value = "/api/courses/{courseId}/activities")
    public ResponseEntity<List<UserActivityResponseDTO>> getActivities(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId) {
        log.debug("COURSE ID: {}", courseId);

        List<Activity> activities = activitiesService.getAllActivitiesByCourse(courseId);

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

    @PreAuthorize("hasAuthority('course_create')")
    @GetMapping(value = "/api/courses/{courseId}/activities/{activityId}")
    public ResponseEntity<ActivityResponseDTO> getActivity(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId, @PathVariable Long activityId) {
        log.debug("COURSE ID: {}", courseId);

        Activity activity = activitiesService.getActivity(activityId);

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity), HttpStatus.OK);
    }
}
