package com.example.rpl.RPL.service


import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.Role
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.UserRepository
import spock.lang.Shared
import spock.lang.Specification

class CoursesServiceSpec extends Specification {

    private CoursesService coursesService
    private CourseUserRepository courseUserRepository
    private CourseRepository courseRepository
    private RoleRepository roleRepository
    private UserRepository userRepository

    @Shared
    private User user


    def setup() {
        courseUserRepository = Mock(CourseUserRepository)
        courseRepository = Mock(CourseRepository)
        roleRepository = Mock(RoleRepository)
        userRepository = Mock(UserRepository)
        coursesService = new CoursesService(
                courseRepository,
                courseUserRepository,
                roleRepository,
                userRepository
        )

        user = Mock(User)
        user.getId() >> 1
    }

    void "should create course successfully"() {
        given:
            String name = "Some new course"
            String universityCourseId = "75.41"
            String description = "Some description"
            String semester = "2c-2019"

        when:
            Course newCourse = coursesService.createCourse(
                    name,
                    universityCourseId,
                    description,
                    true,
                    semester,
                    null,
                    user
            )

        then:
            1 * courseUserRepository.existsByNameAndUniversityCourseIdAndSemesterAndAdmin(name, universityCourseId, semester, 1) >> false
            1 * roleRepository.findByName("admin") >> Optional.of(new Role())

            1 * courseRepository.save(_ as Course) >> { Course course -> return course }

            assert newCourse.name == name
            assert newCourse.universityCourseId == universityCourseId
            assert newCourse.semester == semester
    }

    void "should fail to create course if course exists"() {
        given:
            String name = "Some new course"
            String universityCourseId = "75.41"
            String description = "Some description"
            String semester = "2c-2019"

        when:
            coursesService.createCourse(
                    name,
                    universityCourseId,
                    description,
                    true,
                    semester,
                    null,
                    user
            )

        then:
            1 * courseUserRepository.existsByNameAndUniversityCourseIdAndSemesterAndAdmin(name, universityCourseId, semester, 1) >> true

            thrown(EntityAlreadyExistsException)
    }

    void "should return an empty list of courses when calling getAllCourses"() {
        given: "no courses"
            courseRepository.findAll() >> []

        when: "retrieving all courses"
            List<Course> courses = coursesService.getAllCourses()

        then: "there are no courses"
            courses.isEmpty()
    }

    void "should return some courses when calling getAllCourses"() {
        given: "1 course"
            courseRepository.findAll() >> [
                    new Course()
            ]

        when: "retrieving all course"
            List<Course> courses = coursesService.getAllCourses()

        then: "there is 1 course"
            courses.size() == 1
    }

    void "should return an empty list of courses when calling getAllCoursesByUser"() {
        given: "no course users"
            courseUserRepository.findByUser_Id(1) >> []

        when: "retrieving user courses"
            List<Course> courses = coursesService.getAllCoursesByUser(1)

        then: "there are no courses"
            courses.isEmpty()
    }

    void "should return some courses when calling getAllCoursesByUser"() {
        given: "1 course"
            Course course = Mock(Course)
            CourseUser courseUser = Mock(CourseUser)
            courseUser.getCourse() >> course
            courseUserRepository.findByUser_Id(1) >> [
                    courseUser
            ]

        when: "retrieving all courses"
            List<Course> courses = coursesService.getAllCoursesByUser(1)

        then: "there are no courses"
            courses.size() == 1
    }

    void "should enroll user in course successfully"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(
                    userId,
                    courseId
            )

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.of(new User())
            1 * roleRepository.findByName("student") >> Optional.of(new Role())
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.empty()

            1 * courseUserRepository.save(_)
    }

    void "should throw not found if course not exist"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()
            thrown(NotFoundException)
    }

    void "should throw not found if user not exist"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should throw not found if role not exist"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.of(new User())
            1 * roleRepository.findByName("student") >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should throw entity already exists if user is already registered"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.of(new User())
            1 * roleRepository.findByName("student") >> Optional.of(new Role())
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.of(new Course())

            thrown(EntityAlreadyExistsException)
    }

}
