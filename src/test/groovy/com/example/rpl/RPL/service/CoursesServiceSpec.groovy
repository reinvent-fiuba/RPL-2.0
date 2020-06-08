package com.example.rpl.RPL.service

import com.example.rpl.RPL.controller.ActivityTestsController
import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.Activity
import com.example.rpl.RPL.model.ActivitySubmission
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.CourseUserScore
import com.example.rpl.RPL.model.Role
import com.example.rpl.RPL.model.SubmissionStatus
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.SubmissionRepository
import com.example.rpl.RPL.repository.UserRepository
import spock.lang.Shared
import spock.lang.Specification

import javax.swing.text.html.Option
import java.util.stream.Collectors

class CoursesServiceSpec extends Specification {

    private CoursesService coursesService
    private CourseUserRepository courseUserRepository
    private CourseRepository courseRepository
    private RoleRepository roleRepository
    private UserRepository userRepository
    private ActivitiesService activitiesService
    private SubmissionService submissionService

    @Shared
    private User user


    def setup() {
        courseUserRepository = Mock(CourseUserRepository)
        courseRepository = Mock(CourseRepository)
        roleRepository = Mock(RoleRepository)
        userRepository = Mock(UserRepository)
        activitiesService = Mock(ActivitiesService)
        submissionService = Mock(SubmissionService)
        coursesService = new CoursesService(
                courseRepository,
                courseUserRepository,
                roleRepository,
                userRepository,
                activitiesService,
                submissionService
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
            Long courseAdminId = 1

        when:
            Course newCourse = coursesService.createCourse(
                    name,
                    universityCourseId,
                    description,
                    true,
                    semester,
                    null,
                    courseAdminId
            )

        then:
            1 * courseUserRepository.existsByNameAndUniversityCourseIdAndSemesterAndAdmin(name, universityCourseId, semester, 1) >> false
            1 * roleRepository.findByName("admin") >> Optional.of(new Role())
            1 * userRepository.findById(courseAdminId) >> Optional.of(new User())
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
                    user.getId()
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

    void "should throw not found while enrolling if course not exist"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.enrollInCourse(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()
            thrown(NotFoundException)
    }

    void "should throw not found while enrolling if user not exist"() {
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

    void "should throw not found while enrolling if role not exist"() {
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

    void "should throw entity already exists while enrolling if user is already registered"() {
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

    void "should get all users"() {
        given:
            Long courseId = 1

        when:
            List<CourseUser> courseUsers = coursesService.getAllUsers(courseId, null)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * roleRepository.findByName(null) >> Optional.empty()

            1 * courseUserRepository.findByCourse_Id(courseId) >> [ new CourseUser() ]

            courseUsers.size() == 1
    }

    void "should get all students"() {
        given:
            Long courseId = 1
            String roleName = "student"
            Role studentRole = new Role();
            studentRole.id = 22;

        when:
            List<CourseUser> courseUsers = coursesService.getAllUsers(courseId, roleName)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * roleRepository.findByName(roleName) >> Optional.of(studentRole)

            1 * courseUserRepository.findByCourse_IdAndRole_Id(courseId, studentRole.id) >> [ new CourseUser() ]

            courseUsers.size() == 1
    }

    void "should throw error for wrong course while retrieving users"() {
        given:
            Long courseId = 1

        when:
            List<CourseUser> courseUsers = coursesService.getAllUsers(courseId, null)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should update user correctly"() {
        given:
            Long courseId = 1
            Long userId = 1
            CourseUser tempCourseUser = Mock(CourseUser);
            Role tempRole = Mock(Role);

        when:
            coursesService.updateCourseUser(courseId, userId, accepted, roleName);

        then:
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.of(tempCourseUser)
            if (shouldUpdateAccepted) 1 * tempCourseUser.setAccepted(accepted)
            if (shouldUpdateRole) {
                1 * roleRepository.findByName(roleName) >> Optional.of(new Role())
                1 * tempCourseUser.setRole(_ as Role)
            }
            1 * courseUserRepository.save(_ as CourseUser)

        where:
            accepted | roleName | shouldUpdateAccepted | shouldUpdateRole
            true | null | true | false
            false | null | true | false
            null | "student" | false | true
            true | "student" | true | true
    }

    void "should throw error for missing course user while updating"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            coursesService.updateCourseUser(courseId, userId, true, "student");

        then:
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should not update the role if not present"() {
        given:
            Long courseId = 1
            Long userId = 1
            String roleName = "some-rolename"
            CourseUser tempCourseUser = Mock(CourseUser);

        when:
            coursesService.updateCourseUser(courseId, userId, true, roleName);

        then:
        1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.of(tempCourseUser)
        1 * roleRepository.findByName(roleName) >> Optional.empty()
        0 * tempCourseUser.setRole(_ as Role)
    }

    void "should delete course from user succesfully"() {
        given:
            Long userId = 1
            Long courseId = 1

        when:
            coursesService.deleteCourseUser(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.of(new User())
            1 * courseUserRepository.deleteByCourse_IdAndUser_Id(courseId, userId) >> 1
    }

    void "should throw not found course while deleting course user if course not exist"() {
        given:
            Long userId = 1
            Long courseId = 1

        when:
            coursesService.deleteCourseUser(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()
            thrown(NotFoundException)
    }

    void "should throw not found user while deleting course user if user not exist"() {
        given:
            Long userId = 1
            Long courseId = 1

        when:
            coursesService.deleteCourseUser(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should throw not found course user while deleting course user if user not enrrolled"() {
        given:
            Long userId = 1
            Long courseId = 1

        when:
            coursesService.deleteCourseUser(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * userRepository.findById(userId) >> Optional.of(new User())
            1 * courseUserRepository.deleteByCourse_IdAndUser_Id(courseId, userId) >> 0

            thrown(NotFoundException)
    }

    void "should get user permissions"() {
        given:
            Long courseId = 1
            Long userId = 1
            CourseUser courseUser = Mock(CourseUser)
            Role userRole = Mock(Role)
            def expectedPermisions = ['some', 'permissions']

        when:
            List<String> permissions = coursesService.getPermissions(courseId, userId)

        then:
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.of(courseUser)
            1 * courseUser.getRole() >> userRole
            1 * userRole.getPermissions() >> expectedPermisions

            permissions == expectedPermisions
    }

    void "should throw not found course user while getting permissions if user not enerrolled"() {
        given:
            Long courseId = 1
            Long userId = 1

        when:
            List<String> permissions = coursesService.getPermissions(courseId, userId)

        then:
            1 * courseUserRepository.findByCourse_IdAndUser_Id(courseId, userId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should get simple scoreboard"() {
        given:
            Long courseId = 1
            Activity courseActivity = Mock(Activity)
            User user = Mock(User)
            CourseUser courseUser = Mock(CourseUser)
            ActivitySubmission submission = Mock(ActivitySubmission)
            Role studentRole = Mock(Role)

        when:
            List<CourseUserScore> scoreboard = coursesService.getScoreboard(courseId)

        then:
            1*activitiesService.getAllActivitiesByCourse(courseId) >> [courseActivity]
            1*courseRepository.findById(courseId) >> Optional.of(Mock(Course))
            1*roleRepository.findByName("student") >> Optional.of(studentRole)
            1*studentRole.getId() >> 1
            1*courseUserRepository.findByCourse_IdAndRole_Id(courseId, 1) >> [courseUser]

            1*courseUser.getUser() >> user
            1*submissionService.getAllSubmissionsByUserAndActivities(user, [courseActivity]) >> [submission]
            1*submission.getStatus() >> SubmissionStatus.SUCCESS;
            1*submission.getActivity() >> courseActivity
            1*courseActivity.getPoints() >> 22

            scoreboard[0].getScore() == 22
            scoreboard[0].getActivitiesCount() == 1
    }

    void "should get scoreboard with multiple submissions of same activity"() {
        given:
            Long courseId = 1
            Activity courseActivity = Mock(Activity)
            User user = Mock(User)
            CourseUser courseUser = Mock(CourseUser)
            List<ActivitySubmission> submissions = []
            for (int i=0; i<submissionStatuses.size(); i++) {
                submissions.add(Mock(ActivitySubmission))
            }
            Role studentRole = Mock(Role)

        when:
            List<CourseUserScore> scoreboard = coursesService.getScoreboard(courseId)

        then:
            1*activitiesService.getAllActivitiesByCourse(courseId) >> [courseActivity]
            1*courseRepository.findById(courseId) >> Optional.of(Mock(Course))
            1*roleRepository.findByName("student") >> Optional.of(studentRole)
            1*studentRole.getId() >> 1
            1*courseUserRepository.findByCourse_IdAndRole_Id(courseId, 1) >> [courseUser]

            1*courseUser.getUser() >> user
            1*submissionService.getAllSubmissionsByUserAndActivities(user, [courseActivity]) >> submissions

            for (int i=0; i<submissionStatuses.size(); i++) {
                1 * submissions[i].getStatus() >> submissionStatuses[i]
                if (submissionStatuses[i] == SubmissionStatus.SUCCESS) {
                    1 * submissions[i].getActivity() >> courseActivity
                }
            }

            completedActivities*courseActivity.getPoints() >> 22

            scoreboard[0].getScore() == score
            scoreboard[0].getActivitiesCount() == completedActivities

        where:
            submissionStatuses         | completedActivities | score
            []                         | 0                   | 0
            [SubmissionStatus.PENDING] | 0                   | 0
            [SubmissionStatus.SUCCESS] | 1                   | 22
            [SubmissionStatus.PENDING,
             SubmissionStatus.SUCCESS] | 1                   | 22
            [SubmissionStatus.SUCCESS,
             SubmissionStatus.SUCCESS] | 1                   | 22
    }

    void "should get scoreboard with multiple users"() {
        given:
            Long courseId = 1
            Activity courseActivity = Mock(Activity)

            List<ActivitySubmission> submissions = []
            List<CourseUser> courseUsers = []
            List<User> users = []

            Long numberOfUsers = 5
            for (int i=0; i<numberOfUsers; i++) {
                users.add(Mock(User))
                courseUsers.add(Mock(CourseUser))
                submissions.add(Mock(ActivitySubmission))

            }
            Role studentRole = Mock(Role)

        when:
            List<CourseUserScore> scoreboard = coursesService.getScoreboard(courseId)

        then:
            1*activitiesService.getAllActivitiesByCourse(courseId) >> [courseActivity]
            1*courseRepository.findById(courseId) >> Optional.of(Mock(Course))
            1*roleRepository.findByName("student") >> Optional.of(studentRole)
            1*studentRole.getId() >> 1
            1*courseUserRepository.findByCourse_IdAndRole_Id(courseId, 1) >> courseUsers

            for (int i=0; i<numberOfUsers; i++) {
                1*courseUsers[i].getUser() >> users[i]
                1*submissionService.getAllSubmissionsByUserAndActivities(users[i], [courseActivity]) >> [submissions[i]]
                1 * submissions[i].getStatus() >> SubmissionStatus.SUCCESS
                1 * submissions[i].getActivity() >> courseActivity
                1 * courseActivity.getPoints() >> 22
            }

            scoreboard.size() == numberOfUsers
    }
}
