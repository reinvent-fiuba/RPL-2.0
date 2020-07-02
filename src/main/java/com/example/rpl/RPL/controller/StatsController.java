package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.*;
import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.queue.IProducer;
import com.example.rpl.RPL.repository.TestRunRepository;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.StatsService;
import com.example.rpl.RPL.service.SubmissionService;
import com.example.rpl.RPL.service.TestService;
import com.example.rpl.RPL.utils.TarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.rpl.RPL.model.SubmissionStatus.*;

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
            submissionStats = statsService.getSubmissionStatsByActivity(courseId,categoryId,userId,date);
        } else if (groupBy == GroupBy.user) {
            submissionStats = statsService.getSubmissionStatsByUser(courseId,categoryId,userId,date);
        } else if (groupBy == GroupBy.date) {
            submissionStats = statsService.getSubmissionStatsByDate(courseId, categoryId, userId, date);
        } else {
            submissionStats = statsService.getSubmissionStatsByActivity(courseId,categoryId,userId,date);
        }

        return new ResponseEntity<>(submissionStats, HttpStatus.OK);
    }
}