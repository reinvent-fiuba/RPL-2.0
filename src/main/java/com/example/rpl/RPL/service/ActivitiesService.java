package com.example.rpl.RPL.service;

import static java.time.ZonedDateTime.now;

import com.example.rpl.RPL.exception.BadRequestException;
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
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ActivitiesService {

    private CourseRepository courseRepository;
    private ActivityRepository activityRepository;
    private ActivityCategoryRepository activityCategoryRepository;
    private FileRepository fileRepository;

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
        Boolean active, MultipartFile supportingFile) {

        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        ActivityCategory activityCategory = activityCategoryRepository.findById(activityCategoryId)
            .orElseThrow(
                () -> new NotFoundException("Activity Category not found",
                    "activityCategory_not_found"));

        try {
            RPLFile file = new RPLFile(String.format("%s_%d_%s", now().toString(), courseId, name),
                supportingFile.getContentType(), supportingFile.getBytes());

            fileRepository.save(file);

            Activity activity = new Activity(course, activityCategory, name, description,
                Language.getByName(language), file);

            activityRepository.save(activity);

            return activity;
        } catch (IOException e) {
            log.error("ERROR OBTENIENDO LOS BYTES DEL archivo");
            log.error(e.getMessage());
            throw new BadRequestException("Error obteniendo los bytes del archivo",
                "bad_file");
        }
    }

    public List<Activity> getAllActivitiesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        return activityRepository.findActivitiesByCourse(course);
    }
}
