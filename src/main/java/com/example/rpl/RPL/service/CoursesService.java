package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.CourseUserRepository;
import com.example.rpl.RPL.repository.RoleRepository;
import com.example.rpl.RPL.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.rpl.RPL.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoursesService {

    private CourseRepository courseRepository;
    private CourseUserRepository courseUserRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public CoursesService(CourseRepository courseRepository,
                          CourseUserRepository courseUserRepository,
                          RoleRepository roleRepository,
                          UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.courseUserRepository = courseUserRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new Course.
     * @return a new saved Course
     * @throws EntityAlreadyExistsException if course exists
     *         ValidationException declared on the Course class
     */
    @Transactional
    public Course createCourse(String name, String universityCourseId, String description, Boolean active, String semester, String imgUri, Long userId) {
        if (courseUserRepository.existsByNameAndUniversityCourseIdAndSemesterAndAdmin(name, universityCourseId, semester, userId)) {
            throw new EntityAlreadyExistsException(
                String.format("Course '%s' with id '%s' for '%s' semester already exists", name, universityCourseId, semester),
                "ERROR_COURSE_EXISTS");
        }

        Course course = new Course(name, universityCourseId, description, active, semester, imgUri);
        User user = userRepository.findById(userId).get();
        Role adminRole = roleRepository.findByName("admin").get();
        CourseUser courseUser = new CourseUser(course, user, adminRole, true);

        courseRepository.save(course);
        courseUserRepository.save(courseUser);

        log.info("[process:create_course][name:{}] Creating new course", name);

        return course;
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
