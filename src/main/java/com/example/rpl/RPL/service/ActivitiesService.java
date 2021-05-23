package com.example.rpl.RPL.service;

import static java.time.ZonedDateTime.now;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivityCategory;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.Language;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.repository.ActivityCategoryRepository;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.specification.ActivitySpecifications;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActivitiesService {

    private static final String DEFAULT_GCC_FLAGS = "-g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast";

    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;
    private final ActivityCategoryRepository activityCategoryRepository;
    private final FileRepository fileRepository;

    @Autowired
    public ActivitiesService(CourseRepository courseRepository,
        ActivityRepository activityRepository,
        ActivityCategoryRepository activityCategoryRepository,
        FileRepository fileRepository) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.activityCategoryRepository = activityCategoryRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * Creates a new Activity.
     *
     * @return a new saved Activity
     */
    @Transactional
    public Activity createActivity(Long courseId, Long activityCategoryId, String name,
        String description, String language,
        Boolean active, Long points, String compilationFlags, byte[] startingFilesBytes) {

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
        if (compilationFlags == null && Language.getByName(language) == Language.C) {
            compilationFlags = DEFAULT_GCC_FLAGS;
        }

        Activity activity = new Activity(course, activityCategory, name, description,
            Language.getByName(language), points, file, compilationFlags, active);

        activityRepository.save(activity);

        return activity;
    }

    /**
     * Creates a new Activity.
     *
     * @return a new saved Activity
     */
    @Transactional
    public Activity cloneActivity(Course course, ActivityCategory activityCategory,
        Activity activity) {

        Activity newActivity = new Activity(course, activityCategory, activity);

        fileRepository.save(newActivity.getStartingFiles());

        return activityRepository.save(newActivity);
    }


    @Transactional
    public Activity updateActivity(Activity activity, Long activityCategoryId, String name,
        String description, String languageName, Boolean active, Long points,
        String compilationFlags, byte[] startingFilesBytes) {

        ActivityCategory activityCategory = null;

        if (activityCategoryId != null) {
            activityCategory = activityCategoryRepository.findById(activityCategoryId)
                .orElseThrow(
                    () -> new NotFoundException("Category not found",
                        "category_not_found"));
        }

        RPLFile file = activity.getStartingFiles();

        if (startingFilesBytes != null && !Arrays.equals(file.getData(), startingFilesBytes)) {
            file.updateData(startingFilesBytes);
            fileRepository.save(file);
        }

        Language language = languageName != null ? Language.getByName(languageName) : null;

        activity.updateActivity(activityCategory, name, active, description, language,
            compilationFlags, points);

        activityRepository.save(activity);

        return activity;
    }

    /**
     * Retrieves all non-deleted activities from a course
     *
     * @return a list of Activities Course class
     */
    @Transactional
    public List<Activity> getAllActivitiesByCourse(Long courseId) {
        return activityRepository.findActivitiesByCourse_IdAndDeleted(courseId, false);
    }

    /**
     * Retrieves all activities from a course
     *
     * @return a list of Activities Course class
     */
    @Transactional
    public List<Activity> getAllActiveActivitiesByCourse(Long courseId) {
        return activityRepository
            .findActivitiesByCourse_IdAndActiveAndDeleted(courseId, true, false);
    }


    /**
     * Retrieves all non-deleted activities from a course with a specific category (optional) If
     * cateogoryId is not present, the function ignores the that filter
     *
     * @return a list of Activities Course class
     */
    @Transactional
    public List<Activity> search(Long courseId, Long categoryId, Boolean isActive) {

        Specification<Activity> specification = Specification
            .where(ActivitySpecifications.courseIdIs(courseId))
            .and(categoryId == null ? null : ActivitySpecifications.categoryIdIs(categoryId))
            .and(isActive == null ? null : ActivitySpecifications.isActive(isActive))
            .and(ActivitySpecifications.notDeleted());

        return activityRepository.findAll(specification);
    }

    public Activity getActivity(Long activityId) {
        return activityRepository.findById(activityId)
            .orElseThrow(() -> new NotFoundException("Activity not found",
                "activity_not_found"));
    }

    @Transactional
    public Activity updateTestMode(Activity activity, boolean isIOTested) {
        activity.setIsIOTested(isIOTested);
        return activityRepository.save(activity);
    }

    @Transactional
    public Activity deleteActivity(Long activityId) {
        Activity activity = this.getActivity(activityId);

        activity.setDeleted(true);
        return activityRepository.save(activity);
    }
}
