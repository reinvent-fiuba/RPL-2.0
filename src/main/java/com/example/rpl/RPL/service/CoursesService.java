package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.CourseUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoursesService {

    private CourseRepository courseRepository;
    private CourseUserRepository courseUserRepository;

    @Autowired
    public CoursesService(CourseRepository courseRepository, CourseUserRepository courseUserRepository) {
        this.courseRepository = courseRepository;
        this.courseUserRepository = courseUserRepository;
    }

    /**
     * Get all Courses.
     * @return a new saved User
     */
    @Transactional
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public List<Course> getAllCoursesByUser(Long currentUserId) {
        return courseUserRepository.findByUser_Id(currentUserId)
            .stream()
            .map(CourseUser::getCourse)
            .collect(Collectors.toList());
    }
}
