package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityResponseDTO;
import com.example.rpl.RPL.controller.dto.CourseResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateActivityRequestDTO;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.CoursesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
public class ActivitiesController {

    private ActivitiesService activitiesService;

    @Autowired
    public ActivitiesController(
        ActivitiesService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @PostMapping(value = "/api/courses/{courseId}/activities")
    public ResponseEntity<ActivityResponseDTO> createCourse(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long courseId,
                                                          @Valid CreateActivityRequestDTO createActivityRequestDTO,
                                                          @RequestParam(value = "supportingFile") MultipartFile supportingFile) {


        Activity activity = activitiesService.createActivity(
            courseId,
            createActivityRequestDTO.getActivityCategoryId(),
            createActivityRequestDTO.getName(),
            createActivityRequestDTO.getDescription(),
            createActivityRequestDTO.getLanguage(),
            true,
            supportingFile);


        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity), HttpStatus.CREATED);
    }
}
