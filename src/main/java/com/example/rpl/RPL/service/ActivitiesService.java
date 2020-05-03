package com.example.rpl.RPL.service;

import static java.time.ZonedDateTime.now;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
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
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActivitiesService {

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
     * Creates a new Course.
     *
     * @return a new saved Course
     * @throws EntityAlreadyExistsException if course exists ValidationException declared on the
     * Course class
     */
    @Transactional
    public Activity createActivity(Long courseId, Long activityCategoryId, String name,
        String description, String language,
        Boolean active, String initialCode, byte[] supportingFilesBytes) {

        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        ActivityCategory activityCategory = activityCategoryRepository.findById(activityCategoryId)
            .orElseThrow(
                () -> new NotFoundException("Category not found",
                    "category_not_found"));

        RPLFile file = new RPLFile(
            String.format("%s_%d_%s.tar.gz", now().toString(), courseId, name),
            "application/gzip", supportingFilesBytes);

        fileRepository.save(file);

        Activity activity = new Activity(course, activityCategory, name, description,
            Language.getByName(language), initialCode, file);

        activityRepository.save(activity);

        return activity;
    }

    @Transactional
    public Activity updateActivity(Activity activity, Long activityCategoryId, String name,
        String description, String language,
        Boolean active, String initialCode, byte[] supportingFilesBytes) {

        ActivityCategory activityCategory = activityCategoryRepository.findById(activityCategoryId)
            .orElseThrow(
                () -> new NotFoundException("Category not found",
                    "category_not_found"));

        activity.updateActivity(activityCategory, name, description, Language.getByName(language),
            initialCode);

        activityRepository.save(activity);

        return activity;
    }

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
}
