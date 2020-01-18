package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.BadRequestException;
import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.*;
import com.example.rpl.RPL.repository.ActivityCategoryRepository;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.time.ZonedDateTime.now;

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

        List<ActivityCategory> activityCategories = activityCategoryRepository.findByCourse_Id(courseId);

        return activityCategories;
    }
}
