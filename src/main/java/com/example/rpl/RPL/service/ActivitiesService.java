package com.example.rpl.RPL.service;

import static java.time.ZonedDateTime.now;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.ActivitiesStats;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.Language;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.repository.ActivityCategoryRepository;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.SubmissionRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActivitiesService {

    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;
    private final SubmissionRepository submissionRepository;
    private final ActivityCategoryRepository activityCategoryRepository;
    private final FileRepository fileRepository;

    @Autowired
    public ActivitiesService(CourseRepository courseRepository,
        ActivityRepository activityRepository,
        ActivityCategoryRepository activityCategoryRepository,
        FileRepository fileRepository,
        SubmissionRepository submissionRepository) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.activityCategoryRepository = activityCategoryRepository;
        this.fileRepository = fileRepository;
        this.submissionRepository = submissionRepository;
    }

    /**
     * Creates a new Course.
     *
     * @return a new saved Course
     * @throws EntityAlreadyExistsException if course exists ValidationException declared on the
     * Course class
     */
    @Transactional
    public Activity createActivity(Long courseId, Long activityCategoryId, String name,
        String description, String language,
       Boolean active,  String initialCode, Long points, String compilationFlags, byte[] startingFilesBytes) {

        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        ActivityCategory activityCategory = activityCategoryRepository.findById(activityCategoryId)
            .orElseThrow(
                () -> new NotFoundException("Category not found",
                    "category_not_found"));

        RPLFile file = new RPLFile(
            String.format("%s_%d_%s.tar.gz", now().toLocalDate().toString(), courseId, name),
            "application/gzip", startingFilesBytes);

        fileRepository.save(file);

        // Default compilation flags should be
        // "-g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast"

        Activity activity = new Activity(course, activityCategory, name, description,
            Language.getByName(language), initialCode, points, file, compilationFlags, active);

        activityRepository.save(activity);

        return activity;
    }

    @Transactional
    public Activity updateActivity(Activity activity, Long activityCategoryId, String name,
        String description, String language, Boolean active, String initialCode, Long points,
       String compilationFlags, byte[] startingFilesBytes) {

        ActivityCategory activityCategory = null;

        if (activityCategoryId != null) {
            activityCategory = activityCategoryRepository.findById(activityCategoryId)
                    .orElseThrow(
                            () -> new NotFoundException("Category not found",
                                    "category_not_found"));
        }

        RPLFile file = activity.getStartingFiles();

        if (!Arrays.equals(file.getData(), startingFilesBytes)) {
            file.updateData(startingFilesBytes);
            fileRepository.save(file);
        }

        activity.updateActivity(activityCategory, name, active, description, Language.getByName(language),
            initialCode, compilationFlags, points);

        activityRepository.save(activity);

        return activity;
    }

    @Transactional
    public List<Activity> getAllActivitiesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        return activityRepository.findActivitiesByCourse(course);
    }

    public Activity getActivity(Long activityId) {
        return activityRepository.findById(activityId)
            .orElseThrow(() -> new NotFoundException("Activity not found",
                "activity_not_found"));
    }

    public Activity updateTestMode(Activity activity, boolean isIOTested) {
        activity.setIsIOTested(isIOTested);
        return activityRepository.save(activity);
    }

    public Activity deleteActivity(Long activityId) {
        Activity activity = this.getActivity(activityId);

        activity.setDeleted(true);
        return activityRepository.save(activity);
    }

    public Activity disableActivity(Long activityId, Boolean active) {
        Activity activity = this.getActivity(activityId);

        activity.setActive(active);
        log.error("Setting activity as " + active);
        return activityRepository.save(activity);
    }

    @Transactional
    public ActivitiesStats getActivitiesStatsByUserAndCourseId(Long userId, Long courseId) {
        List<Activity> activities = getAllActivitiesByCourse(courseId);
        int total = activities.size();
        List<ActivitySubmission> activitySubmissions = submissionRepository.findAllByUserIdAndCourseId(userId, courseId);

        Map<Long, List<ActivitySubmission>> submissionsByActivity = activitySubmissions.stream()
            .collect(Collectors.groupingBy(activitySubmission -> activitySubmission.getActivity().getId()));

        HashMap<String, Long> countByStatus = new HashMap<>();
        countByStatus.put("STARTED", (long) 0);
        countByStatus.put("NON STARTED", (long) 0);
        countByStatus.put("SOLVED", (long) 0);

        HashMap<String, Long> score = new HashMap<>();
        score.put("OBTAINED", (long) 0);
        score.put("PENDING", (long) 0);

        for (Activity activity : activities) {
            List<ActivitySubmission> submissions = submissionsByActivity.get(activity.getId());
            if (submissions == null) {
                countByStatus.put("NON STARTED", countByStatus.get("NON STARTED") + 1);
                score.put("PENDING", score.get("PENDING") + activity.getPoints());
            } else if (submissions.stream().anyMatch(activitySubmission -> activitySubmission.getStatus().toString() == "SUCCESS")) {
                countByStatus.put("SOLVED", countByStatus.get("SOLVED") + 1);
                score.put("OBTAINED", score.get("OBTAINED") + activity.getPoints());
            } else {
                countByStatus.put("STARTED", countByStatus.get("STARTED") + 1);
                score.put("PENDING", score.get("PENDING") + activity.getPoints());
            }
        }

        return new ActivitiesStats(total, countByStatus, score);
    }
}
