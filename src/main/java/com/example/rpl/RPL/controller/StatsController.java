package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivitiesStatResponseDTO;
import com.example.rpl.RPL.controller.dto.SubmissionsStatResponseDTO;
import com.example.rpl.RPL.controller.dto.SubmissionsStatsResponseDTO;
import com.example.rpl.RPL.model.stats.ActivitiesStat;
import com.example.rpl.RPL.model.stats.SubmissionsStat;
import com.example.rpl.RPL.model.stats.SubmissionsStats;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.StatsService;
import com.example.rpl.RPL.service.SubmissionService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StatsController {

    private enum GroupBy {
        date, user, activity
    }

    private final SubmissionService submissionService;
    private final StatsService statsService;

    @Autowired
    public StatsController(SubmissionService submissionService,
                           StatsService statsService) {
        this.submissionService = submissionService;
        this.statsService = statsService;
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @GetMapping(value = "/api/stats/courses/{courseId}/submissions")
    public ResponseEntity<SubmissionsStatsResponseDTO> getSubmissionsStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) GroupBy groupBy
    ) {
        SubmissionsStats submissionsStats;

        if (groupBy == GroupBy.activity) {
            submissionsStats = statsService.getSubmissionStatsGroupByActivity(courseId,categoryId,userId, activityId, date);
        } else if (groupBy == GroupBy.user) {
            submissionsStats = statsService.getSubmissionStatsGroupByUser(courseId,categoryId, userId, activityId, date);
        } else if (groupBy == GroupBy.date) {
            submissionsStats = statsService
                .getStudentSubmissionStatsGroupByDate(courseId, categoryId, userId, activityId, date);
        } else {
            submissionsStats = statsService.getSubmissionStatsGroupByActivity(courseId,categoryId,userId, activityId, date);
        }

        return new ResponseEntity<>(SubmissionsStatsResponseDTO.fromEntity(submissionsStats), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/stats/courses/{courseId}/activities/me")
    public ResponseEntity<ActivitiesStatResponseDTO> getMyActivityStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId
    ) {
        ActivitiesStat activitiesStat = statsService.getActivityStatByUser(courseId, currentUser.getUser().getId());

        return new ResponseEntity<>(ActivitiesStatResponseDTO.fromEntity(activitiesStat), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/stats/courses/{courseId}/submissions/me")
    public ResponseEntity<SubmissionsStatResponseDTO> getMySubmissionsStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId
    ) {
        SubmissionsStat submissionsStat = statsService.getSubmissionStatByUser(courseId, currentUser.getUser().getId());

        return new ResponseEntity<>(SubmissionsStatResponseDTO.fromEntity(submissionsStat), HttpStatus.OK);
    }
}
