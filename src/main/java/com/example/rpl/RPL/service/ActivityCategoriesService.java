package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.ActivityCategory;
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

    private CourseRepository courseRepository;
    private ActivityCategoryRepository activityCategoryRepository;

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
}
