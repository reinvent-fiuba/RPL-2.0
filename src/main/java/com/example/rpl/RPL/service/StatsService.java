package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.model.stats.ActivityStat;
import com.example.rpl.RPL.model.stats.SubmissionStat;
import com.example.rpl.RPL.model.stats.SubmissionStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public SubmissionStats getSubmissionStatsGroupByActivity(Long courseId, Long categoryId, Long userId, LocalDate date) {

        List<Activity> activities = activitiesService.getAllActivitiesByCourse(courseId, categoryId);

        List<ActivitySubmission> submissions = submissionService.getAllSubmissionsByActivities(activities, userId, date);

        Map<Activity, List<ActivitySubmission>> submissionsByActivitysubmissions = submissions.stream()
                .collect(Collectors.groupingBy(ActivitySubmission::getActivity));

        List<Map<String,String>> activitiesMetadata = activities.stream()
                .map(activity -> Map.of(
                        "id", activity.getId().toString(),
                        "name", activity.getName(),
                        "points", activity.getPoints().toString(),
                        "category_name", activity.getActivityCategory().getName()
                )).collect(Collectors.toCollection(ArrayList::new));

        List<SubmissionStat> submissionStats = new ArrayList<>();

        for (Activity activity : activities) {
            submissionStats.add(new SubmissionStat(
                    submissionsByActivitysubmissions.getOrDefault(activity, new ArrayList<>())
            ));
        }

        return new SubmissionStats(submissionStats, activitiesMetadata);
    }

    public SubmissionStats getSubmissionStatsGroupByUser(Long courseId, Long categoryId, Long userId, LocalDate date) {

        List<CourseUser> courseUsers = userId != null ?
                List.of(coursesService.getStudentByUserId(courseId, userId)) :
                coursesService.getAllUsers(courseId, "student");

        List<ActivitySubmission> submissions = submissionService.getAllSubmissionsByCourseId(courseId, userId, date);

        Map<User, List<ActivitySubmission>> submissionsByUser = submissions.stream()
                .collect(Collectors.groupingBy(activitySubmission -> activitySubmission.getUser()));

        List<Map<String,String>> usersMetadata = courseUsers.stream()
                .map(courseUser -> Map.of(
                        "id", courseUser.getUser().getId().toString(),
                        "course_user_id", courseUser.getId().toString(),
                        "name", courseUser.getUser().getName(),
                        "surname", courseUser.getUser().getSurname(),
                        "username", courseUser.getUser().getUsername(),
                        "student_id", courseUser.getUser().getStudentId()
                )).collect(Collectors.toCollection(ArrayList::new));

        List<SubmissionStat> submissionStats = new ArrayList<>();

        for (CourseUser courseUser : courseUsers) {
            User user = courseUser.getUser();
            submissionStats.add(new SubmissionStat(
                    submissionsByUser.getOrDefault(user, new ArrayList<>())
            ));
        }

        return new SubmissionStats(submissionStats, usersMetadata);
    }

    public SubmissionStats getSubmissionStatsGroupByDate(Long courseId, Long categoryId, Long userId, LocalDate date) {
        List<ActivitySubmission> submissions = submissionService.getAllSubmissionsByCourseId(courseId, userId, date);

        Map<LocalDate, List<ActivitySubmission>> submissionsByDate =
                submissions.stream().collect(Collectors.groupingBy(submission -> submission.getDateCreated().toLocalDate()));


        List<Map<String,String>> dateMetadata = new ArrayList<>();
        List<SubmissionStat> submissionStats = new ArrayList<>();

        for (LocalDate submissionStatDate : submissionsByDate.keySet()) {
            dateMetadata.add(Map.of("date", submissionStatDate.toString()));
            submissionStats.add(new SubmissionStat(submissionsByDate.get(submissionStatDate)));
        }

        return new SubmissionStats(submissionStats, dateMetadata);
    }

    public ActivityStat getActivityStatByUser(Long courseId, Long userId) {
        List<Activity> activities = activitiesService.getAllActivitiesByCourse(courseId);
        List<ActivitySubmission> activitySubmissions = submissionService.getAllSubmissionsByCourseId(courseId, userId);

        Map<Long, List<ActivitySubmission>> submissionsByActivity = activitySubmissions.stream()
                .collect(Collectors.groupingBy(activitySubmission -> activitySubmission.getActivity().getId()));

        long started, notStarted, solved, pendingPoints, obtainedPoints;
        started = notStarted = solved = pendingPoints = obtainedPoints = 0;

        for (Activity activity : activities) {
            List<ActivitySubmission> submissions = submissionsByActivity.get(activity.getId());
            if (submissions == null) {
                notStarted++;
                pendingPoints += activity.getPoints();
            } else if (submissions.stream().anyMatch(activitySubmission -> activitySubmission.getStatus().toString() == "SUCCESS")) {
                solved++;
                obtainedPoints += activity.getPoints();
            } else {
                started++;
                pendingPoints += activity.getPoints();
            }
        }

        ActivityStat activityStat = new ActivityStat(started, notStarted, solved, pendingPoints, obtainedPoints);

        return activityStat;
    }

    public SubmissionStat getSubmissionStatByUser(Long courseId, Long userId) {
        List<ActivitySubmission> activitySubmissions = submissionService.getAllSubmissionsByCourseId(courseId, userId);

        return new SubmissionStat(activitySubmissions);
    }
}
