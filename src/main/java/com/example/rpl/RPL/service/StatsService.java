package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.stats.ActivitiesStat;
import com.example.rpl.RPL.model.stats.SubmissionsStat;
import com.example.rpl.RPL.model.stats.SubmissionsStats;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatsService {

    private final ActivitiesService activitiesService;
    private final SubmissionService submissionService;
    private final CoursesService coursesService;

    @Autowired
    public StatsService(ActivitiesService activitiesService,
        SubmissionService submissionService,
        CoursesService coursesService) {
        this.activitiesService = activitiesService;
        this.submissionService = submissionService;
        this.coursesService = coursesService;
    }

    public SubmissionsStats getSubmissionStatsGroupByActivity(Long courseId, Long categoryId,
        Long userId, Long activityId, LocalDate date) {

        List<Activity> activities = activityId != null ?
            List.of(activitiesService.getActivity(activityId)) :
            activitiesService.search(courseId, categoryId, true);

        List<ActivitySubmission> submissions = submissionService
            .getAllSubmissionsByActivities(activities, userId, null, date);

        Map<Activity, List<ActivitySubmission>> submissionsByActivity = submissions.stream()
            .collect(Collectors.groupingBy(ActivitySubmission::getActivity));

        List<Map<String, String>> activitiesMetadata = activities.stream()
            .map(activity -> Map.of(
                "id", activity.getId().toString(),
                "name", activity.getName(),
                "points", activity.getPoints().toString(),
                "category_name", activity.getActivityCategory().getName()
            )).collect(Collectors.toCollection(ArrayList::new));

        List<SubmissionsStat> submissionsStats = new ArrayList<>();

        for (Activity activity : activities) {
            submissionsStats.add(new SubmissionsStat(
                submissionsByActivity.getOrDefault(activity, new ArrayList<>())
            ));
        }

        return new SubmissionsStats(submissionsStats, activitiesMetadata);
    }

    @Cacheable(value = "submissionsCalendarByUser", cacheManager = "defaultCacheManager")
    public SubmissionsStats getSubmissionStatsGroupByUser(Long courseId, Long categoryId,
        Long userId, Long activityId, LocalDate date) {

        List<CourseUser> courseUsers = userId != null ?
            List.of(coursesService.getStudentByUserId(courseId, userId)) :
            coursesService.getAllUsers(courseId, "student");

        List<ActivitySubmission> submissions = submissionService
            .search(courseId, userId, activityId, categoryId, date);

        Map<User, List<ActivitySubmission>> submissionsByUser = submissions.stream()
            .collect(Collectors.groupingBy(ActivitySubmission::getUser));

        List<Map<String, String>> usersMetadata = courseUsers.stream()
            .map(courseUser -> Map.of(
                "id", courseUser.getUser().getId().toString(),
                "course_user_id", courseUser.getId().toString(),
                "name", courseUser.getUser().getName(),
                "surname", courseUser.getUser().getSurname(),
                "username", courseUser.getUser().getUsername(),
                "student_id", courseUser.getUser().getStudentId()
            )).collect(Collectors.toCollection(ArrayList::new));

        List<SubmissionsStat> submissionsStats = new ArrayList<>();

        for (CourseUser courseUser : courseUsers) {
            User user = courseUser.getUser();
            submissionsStats.add(new SubmissionsStat(
                submissionsByUser.getOrDefault(user, new ArrayList<>())
            ));
        }

        return new SubmissionsStats(submissionsStats, usersMetadata);
    }

    @Cacheable(value = "submissionsCalendarByDate", cacheManager = "defaultCacheManager")
    public SubmissionsStats getStudentSubmissionStatsGroupByDate(Long courseId, Long categoryId,
        Long userId, Long activityId, LocalDate date) {
        List<CourseUser> courseUsers = userId != null ?
            List.of(coursesService.getStudentByUserId(courseId, userId)) :
            coursesService.getAllUsers(courseId, "student");

        List<User> users = courseUsers.stream().map(CourseUser::getUser)
            .collect(Collectors.toList());

        List<ActivitySubmission> submissions = submissionService
            .search(courseId, userId, activityId, categoryId, date);

        List<ActivitySubmission> studentSubmissions = submissions.stream()
            .filter(submission -> users.contains(submission.getUser())).collect(
                Collectors.toList());

        Map<LocalDate, List<ActivitySubmission>> submissionsByDate =
            studentSubmissions.stream().collect(
                Collectors.groupingBy(submission -> submission.getDateCreated().toLocalDate()));

        List<Map<String, String>> dateMetadata = new ArrayList<>();
        List<SubmissionsStat> submissionsStats = new ArrayList<>();

        for (LocalDate submissionStatDate : submissionsByDate.keySet()) {
            dateMetadata.add(Map.of("date", submissionStatDate.toString()));
            submissionsStats.add(new SubmissionsStat(submissionsByDate.get(submissionStatDate)));
        }

        return new SubmissionsStats(submissionsStats, dateMetadata);
    }

    public ActivitiesStat getActivityStatByUser(Long courseId, Long userId) {
        List<Activity> activities = activitiesService.getAllActiveActivitiesByCourse(courseId);
        List<ActivitySubmission> activitySubmissions = submissionService
            .search(courseId, userId, null, null, null);

        Map<Long, List<ActivitySubmission>> submissionsByActivity = activitySubmissions.stream()
            .collect(Collectors
                .groupingBy(activitySubmission -> activitySubmission.getActivity().getId()));

        long started, notStarted, solved, pendingPoints, obtainedPoints;
        started = notStarted = solved = pendingPoints = obtainedPoints = 0;

        for (Activity activity : activities) {
            List<ActivitySubmission> submissions = submissionsByActivity.get(activity.getId());
            if (submissions == null) {
                notStarted++;
                pendingPoints += activity.getPoints();
            } else if (submissions.stream().anyMatch(
                activitySubmission -> activitySubmission.getStatus().toString()
                    .equals("SUCCESS"))) {
                solved++;
                obtainedPoints += activity.getPoints();
            } else {
                started++;
                pendingPoints += activity.getPoints();
            }
        }

        ActivitiesStat activitiesStat = new ActivitiesStat(started, notStarted, solved,
            pendingPoints, obtainedPoints);

        return activitiesStat;
    }

    public SubmissionsStat getSubmissionStatByUser(Long courseId, Long userId) {
        List<ActivitySubmission> activitySubmissions = submissionService
            .search(courseId, userId, null, null, null);

        return new SubmissionsStat(activitySubmissions);
    }
}
