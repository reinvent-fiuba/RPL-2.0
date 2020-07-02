package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityCategoryResponseDTO;
import com.example.rpl.RPL.controller.dto.ActivityStatsResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateActivityCategoryRequestDTO;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.SubmissionStat;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivityCategoriesService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ActivityCategoriesController {

    private final ActivityCategoriesService activityCategoriesService;

    @Autowired
    public ActivityCategoriesController(
        ActivityCategoriesService activityCategoriesService) {
        this.activityCategoriesService = activityCategoriesService;
    }

    @PreAuthorize("hasAuthority('activity_view')")
    @GetMapping(value = "/api/courses/{courseId}/activityCategories")
    public ResponseEntity<List<ActivityCategoryResponseDTO>> getActivityCategories(@CurrentUser UserPrincipal currentUser,
                                                                     @PathVariable Long courseId) {


        List<ActivityCategory> activityCategories = activityCategoriesService.getActivityCategories(courseId);

        return new ResponseEntity<>(
            activityCategories.stream()
                .map(ActivityCategoryResponseDTO::fromEntity)
                .collect(Collectors.toList())
            , HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PostMapping(value = "/api/courses/{courseId}/activityCategories")
    public ResponseEntity<ActivityCategoryResponseDTO> getActivityCategories(@CurrentUser UserPrincipal currentUser,
                                                                                   @RequestBody @Valid CreateActivityCategoryRequestDTO createActivityCategoryRequestDTO,
                                                                                   @PathVariable Long courseId) {


        ActivityCategory activityCategory = activityCategoriesService.createActivityCategory(courseId, createActivityCategoryRequestDTO.getName(), createActivityCategoryRequestDTO.getDescription());

        return new ResponseEntity<>(
            ActivityCategoryResponseDTO.fromEntity(activityCategory),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @GetMapping(value = "/api/courses/{courseId}/activityCategories/{categoryId}/stats")
    public ResponseEntity<List<ActivityStatsResponseDTO>> getActivityCategoriesStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId,
            @PathVariable Long categoryId,
            @RequestParam(required = false) Long courseUserId) {

        List<SubmissionStat> submissionStats;

        if (courseUserId != null) {
            submissionStats = activityCategoriesService.getActivityCategoryStatsByCategoryAndCourseUser(courseId, categoryId, courseUserId);
        } else {
            submissionStats = activityCategoriesService.getActivityCategoryStatsByCategory(courseId, categoryId);
        }

        return new ResponseEntity<>(
                submissionStats.stream()
                        .map(ActivityStatsResponseDTO::fromEntity)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
