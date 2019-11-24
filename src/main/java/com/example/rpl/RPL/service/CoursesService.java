package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.model.CourseSemester;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.CourseSemesterRepository;
import com.example.rpl.RPL.repository.CourseUserRepository;
import com.example.rpl.RPL.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CoursesService {

    private CourseSemesterRepository courseSemesterRepository;
    private CourseUserRepository courseUserRepository;

    @Autowired
    public CoursesService(CourseSemesterRepository courseSemesterRepository, CourseUserRepository courseUserRepository) {
        this.courseSemesterRepository = courseSemesterRepository;
        this.courseUserRepository = courseUserRepository;
    }

    /**
     * Get all Courses.
     * @return a new saved User
     */
    @Transactional
    public List<CourseSemester> getAllCoursesSemester() {
        return courseSemesterRepository.findAll();
    }
}
