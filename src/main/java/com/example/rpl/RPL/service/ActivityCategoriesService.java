package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.repository.ActivityCategoryRepository;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActivityCategoriesService {

    private final CourseRepository courseRepository;
    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityRepository activityRepository;
    private final SubmissionService submissionService;
    private final CoursesService coursesService;

    @Autowired
    public ActivityCategoriesService(CourseRepository courseRepository,
                                     ActivityCategoryRepository activityCategoryRepository,
                                     ActivityRepository activityRepository,
                                     SubmissionService submissionService,
                                     CoursesService coursesService) {
        this.courseRepository = courseRepository;
        this.activityCategoryRepository = activityCategoryRepository;
        this.activityRepository = activityRepository;
        this.submissionService = submissionService;
        this.coursesService = coursesService;
    }

    @Transactional
    public List<ActivityCategory> getActivityCategories(Long courseId) {

        courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        return activityCategoryRepository.findByCourse_Id(courseId);
    }

    @Transactional
    public ActivityCategory createActivityCategory(Long courseId, String name, String description) {

        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        ActivityCategory activityCategory = new ActivityCategory(course, name, description, true);

        activityCategoryRepository.save(activityCategory);

        return activityCategory;
    }

    @Transactional
    public List<ActivityStats> getActivityCategoryStatsByCategoryAndCourseUser(Long courseId, Long categoryId, Long courseUserId) {

        CourseUser courseUser = coursesService.getCourseUser(courseUserId);
        List<Activity> activities = activityRepository.findActivitiesByActivityCategory_IdAndCourse_Id(categoryId, courseId);

        List<ActivitySubmission> submissions = submissionService.getAllSubmissionsByUserAndActivities(courseUser.getUser(), activities);

        List<ActivityStats> activityStats = new ArrayList<>();

        Map<Activity, List<ActivitySubmission>> submissionsByActivitysubmissions = submissions.stream()
                .collect(Collectors.groupingBy(ActivitySubmission::getActivity))
        ;

        for (Activity activity : activities) {
            activityStats.add(new ActivityStats(
                    activity,
                    submissionsByActivitysubmissions.getOrDefault(activity, new ArrayList<>())
            ));
        }

        return activityStats;
    }

    @Transactional
    public List<ActivityStats> getActivityCategoryStatsByCategory(Long courseId, Long categoryId) {

        List<Activity> activities = activityRepository.findActivitiesByActivityCategory_IdAndCourse_Id(categoryId, courseId);

        List<ActivitySubmission> submissions = submissionService.getAllSubmissionsByActivities(activities);

        List<ActivityStats> activityStats = new ArrayList<>();

        Map<Activity, List<ActivitySubmission>> submissionsByActivitysubmissions = submissions.stream()
                .collect(Collectors.groupingBy(ActivitySubmission::getActivity))
                ;

        for (Activity activity : activities) {
            activityStats.add(new ActivityStats(
                    activity,
                    submissionsByActivitysubmissions.getOrDefault(activity, new ArrayList<>())
            ));
        }

        return activityStats;
    }
}
