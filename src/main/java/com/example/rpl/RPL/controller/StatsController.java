package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.model.stats.ActivityStat;
import com.example.rpl.RPL.model.stats.SubmissionStat;
import com.example.rpl.RPL.model.stats.SubmissionStats;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.StatsService;
import com.example.rpl.RPL.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/stats/courses/{courseId}/submissions")
    public ResponseEntity<SubmissionStats> getSubmissionsStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) GroupBy groupBy
    ) {
        SubmissionStats submissionStats;

        if (groupBy == GroupBy.activity) {
            submissionStats = statsService.getSubmissionStatsGroupByActivity(courseId,categoryId,userId,date);
        } else if (groupBy == GroupBy.user) {
            submissionStats = statsService.getSubmissionStatsGroupByUser(courseId,categoryId,userId,date);
        } else if (groupBy == GroupBy.date) {
            submissionStats = statsService.getSubmissionStatsGroupByDate(courseId, categoryId, userId, date);
        } else {
            submissionStats = statsService.getSubmissionStatsGroupByActivity(courseId,categoryId,userId,date);
        }

        return new ResponseEntity<>(submissionStats, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/stats/courses/{courseId}/activities/me")
    public ResponseEntity<ActivityStat> getMyActivityStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId
    ) {
        ActivityStat activityStat = statsService.getActivityStatByUser(courseId, currentUser.getUser().getId());

        return new ResponseEntity<>(activityStat, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_submit')")
    @GetMapping(value = "/api/stats/courses/{courseId}/submissions/me")
    public ResponseEntity<SubmissionStat> getMySubmissionsStats(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long courseId
    ) {
        SubmissionStat submissionStat = statsService.getSubmissionStatByUser(courseId, currentUser.getUser().getId());

        return new ResponseEntity<>(submissionStat, HttpStatus.OK);
    }
}