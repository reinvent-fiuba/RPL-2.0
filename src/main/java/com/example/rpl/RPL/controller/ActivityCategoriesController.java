package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityCategoryResponseDTO;
import com.example.rpl.RPL.controller.dto.ActivityResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateActivityRequestDTO;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.ActivityCategoriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ActivityCategoriesController {

    private ActivityCategoriesService activityCategoriesService;

    @Autowired
    public ActivityCategoriesController(
        ActivityCategoriesService activityCategoriesService) {
        this.activityCategoriesService = activityCategoriesService;
    }

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
}
