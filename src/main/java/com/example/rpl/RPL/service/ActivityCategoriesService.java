package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.repository.ActivityCategoryRepository;
import com.example.rpl.RPL.repository.CourseRepository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActivityCategoriesService {

    private final CourseRepository courseRepository;
    private final ActivityCategoryRepository activityCategoryRepository;

    @Autowired
    public ActivityCategoriesService(CourseRepository courseRepository,
                                     ActivityCategoryRepository activityCategoryRepository) {
        this.courseRepository = courseRepository;
        this.activityCategoryRepository = activityCategoryRepository;
    }

    @Transactional
    public List<ActivityCategory> getActivityCategories(Long courseId) {

        courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        return activityCategoryRepository.findByCourse_Id(courseId);
    }

    @Transactional
    public ActivityCategory cloneActivityCategory(Course course, ActivityCategory activityCategory) {

        ActivityCategory newActivityCategory = new ActivityCategory(course, activityCategory);
        return activityCategoryRepository.save(newActivityCategory);
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
    public ActivityCategory updateActivityCategory(Long courseId,
                                                   Long activityCategoryId,
                                                   String name,
                                                   String description,
                                                   Boolean active) {

        ActivityCategory activityCategory = activityCategoryRepository.findByCourse_IdAndId(courseId, activityCategoryId).orElseThrow(
                () -> new NotFoundException("Activity category not found",
                        "activity_category_not_found")
        );

        if (name != null) {
            activityCategory.setName(name);
        }

        if (description != null) {
            activityCategory.setDescription(description);
        }

        if (active != null) {
            activityCategory.setActive(active);
        }

        activityCategoryRepository.save(activityCategory);

        return activityCategory;
    }
}
