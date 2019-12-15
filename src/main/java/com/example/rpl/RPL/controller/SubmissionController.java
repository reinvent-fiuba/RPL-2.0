package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivitySubmissionDTO;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SubmissionController {

    private SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping(value = "/api/submissions/{submissionId}")
    public ResponseEntity<ActivitySubmissionDTO> getSubmission(@PathVariable Long submissionId) {
        log.error("SUBMISSION ID ID: {}", submissionId);

        ActivitySubmission as = submissionService.getActivitySubmission(submissionId);

        ActivitySubmissionDTO asDto = ActivitySubmissionDTO.fromEntity(as);

        return new ResponseEntity<>(asDto, HttpStatus.OK);
    }

}