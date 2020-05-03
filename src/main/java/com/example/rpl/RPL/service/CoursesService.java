package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.CourseRepository;
import com.example.rpl.RPL.repository.CourseUserRepository;
import com.example.rpl.RPL.repository.RoleRepository;
import com.example.rpl.RPL.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CoursesService {

    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

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
     *
     * @return a new saved Course
     * @throws EntityAlreadyExistsException if course exists ValidationException declared on the
     * Course class
     */
    @Transactional
    public Course createCourse(String name, String universityCourseId, String description,
        Boolean active, String semester, String imgUri, User user) {
        if (courseUserRepository
            .existsByNameAndUniversityCourseIdAndSemesterAndAdmin(name, universityCourseId,
                semester, user.getId())) {
            throw new EntityAlreadyExistsException(
                String.format("Course '%s' with id '%s' for '%s' semester already exists", name,
                    universityCourseId, semester),
                "ERROR_COURSE_EXISTS");
        }

        Course course = new Course(name, universityCourseId, description, active, semester, imgUri);

        Role adminRole = roleRepository.findByName("admin")
            .orElseThrow(() -> new NotFoundException("role_not_found"));
        CourseUser courseUser = new CourseUser(course, user, adminRole, true);

        courseRepository.save(course);
        courseUserRepository.save(courseUser);

        log.info("[process:create_course][name:{}] Creating new course", name);

        return course;
    }

    /**
     * Get all Courses.
     *
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

    @Transactional
    public CourseUser enrollInCourse(Long currentUserId, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        User user = userRepository.findById(currentUserId).orElseThrow(
            () -> new NotFoundException("User not found",
                "user_not_found"));

        Role studentRole = roleRepository.findByName("student").orElseThrow(
            () -> new NotFoundException("Role not found",
                "role_not_fond"));

        if (courseUserRepository.findByCourse_IdAndUser_Id(courseId, currentUserId).isPresent()) {
            throw new EntityAlreadyExistsException("User is already registered in the course");
        }

        CourseUser courseUser = new CourseUser(
            course,
            user,
            studentRole,
            true
        );

        courseUser = courseUserRepository.save(courseUser);

        return courseUser;
    }

    @Transactional
    public void deleteCourseUser(Long userId, Long courseId) {
        // TODO: Review if this checks are needed
        Course course = courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("User not found",
                "user_not_found"));

        if (courseUserRepository.deleteByCourse_IdAndUser_Id(courseId, userId) == 0) {
            throw new NotFoundException("User not found in course");
        }
    }

    @Transactional
    public List<CourseUser> getAllUsers(Long courseId, String roleName) {
        courseRepository.findById(courseId).orElseThrow(
            () -> new NotFoundException("Course not found",
                "course_not_found"));

        Optional<Role> role = roleRepository.findByName(roleName);

        return role.isPresent() ?
            courseUserRepository.findByCourse_IdAndRole_Id(courseId, role.get().getId()) :
            courseUserRepository.findByCourse_Id(courseId);
    }

    @Transactional
    public CourseUser updateCourseUser(Long courseId, Long userId, Boolean accepted, String roleName) {
        // TODO: Update courseUser all at once without using a SELECT at first
        CourseUser courseUser = courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId).orElseThrow(
            () -> new NotFoundException("Enrolled User not found in this Course",
                    "course_user_not_found")
        );

        if (accepted != null) {
            courseUser.setAccepted(accepted);
        }

        Optional<Role> role;
        if (roleName != null && (role = roleRepository.findByName(roleName)).isPresent()) {
            courseUser.setRole(role.get());
        }

        courseUserRepository.save(courseUser);

        return courseUser;
    }

    @Transactional
    public List<String> getPermissions(Long courseId, Long userId) {
        CourseUser courseUser = courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId).orElseThrow(
                () -> new NotFoundException("Enrolled User not found in this Course",
                        "course_user_not_found")
        );

        return courseUser.getRole().getPermissions();
    }
}
