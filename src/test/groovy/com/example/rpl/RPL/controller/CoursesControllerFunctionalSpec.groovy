package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.Activity
import com.example.rpl.RPL.model.ActivityCategory
import com.example.rpl.RPL.model.ActivitySubmission
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.Language
import com.example.rpl.RPL.model.RPLFile
import com.example.rpl.RPL.model.Role
import com.example.rpl.RPL.model.SubmissionStatus
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.ActivityCategoryRepository
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.FileRepository
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.SubmissionRepository
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import java.time.ZonedDateTime

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class CoursesControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    CourseUserRepository courseUserRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    RoleRepository roleRepository

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    ActivityRepository activityRepository

    @Autowired
    ActivityCategoryRepository activityCategoryRepository

    @Autowired
    FileRepository fileRepository


    String username

    String password

    String otherUsername

    String otherPassword

    Long courseId

    User otherUser

    Role adminRole

    @Shared
    Role studentRole

    @Shared
    User user

    @Shared
    Course course


    def setup() {
        user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                passwordEncoder.encode('supersecret'),
                'some-university',
                'some-hard-degree'
        )
        user.setIsAdmin(true);
        user.markAsValidated()

        username = 'username'
        password = 'supersecret'

        userRepository.save(user)

        otherUser = new User(
                'other-name',
                'other-surname',
                'other-student-id',
                'otheruser',
                'other@mail.com',
                passwordEncoder.encode('supersecret'),
                'other-university',
                'other-hard-degree'
        )
        otherUser.markAsValidated()

        otherUsername = 'otheruser'
        otherPassword = 'supersecret'

        userRepository.save(otherUser)

        course = new Course(
                "some-course",
                "fiuba",
                "some-university-id",
                "some-description",
                true,
                "2019-2c",
                ZonedDateTime.now(),
                ZonedDateTime.now(),
                "/some/uri"
        )

        course = courseRepository.save(course);

        courseId = course.getId()

        adminRole = new Role(
                "admin",
                "course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage"
        )

        roleRepository.save(adminRole);

        studentRole = new Role('student', 'course_view,activity_view,activity_submit,user_view')

        roleRepository.save(studentRole)

        courseUserRepository.save(new CourseUser(
                course,
                user,
                adminRole,
                true
        ))
    }

    def cleanup() {
        submissionRepository.deleteAll()
        activityRepository.deleteAll()
        fileRepository.deleteAll()
        activityCategoryRepository.deleteAll()
        courseUserRepository.deleteAll()
        userRepository.deleteAll()
        courseRepository.deleteAll()
        roleRepository.deleteAll()
    }

    /*****************************************************************************************
     ********** CREATE COURSE ****************************************************************
     *****************************************************************************************/

    @Unroll
    void "test create course with correct values should save course in DB"() {
        given: "a new course"
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    name                : 'Some new course',
                    university_course_id: '75.41',
                    university          : 'UBA',
                    description         : 'An awesome description',
                    semester            : "2019-2c",
                    semester_start_date : "2020-05-22",
                    semester_end_date   : "2020-09-22",
                    course_admin_id     : user.getId()
            ]

        when: "post new course"
            def response = post("/api/courses", body, [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Course"
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map course = getJsonResponse(response)

            assert course.id != null
            assert course.name == body.name
            assert course.university_course_id == body.university_course_id
            assert course.description == body.description
            assert course.semester == body.semester

            assert courseUserRepository.findByCourse_IdAndUser_Id(course.id, user.getId()).isPresent()

            assert courseRepository.existsById(course.id as Long)
    }

    @Unroll
    void "test create course with correct values with other admin should save course in DB"() {
        given: "a new course"
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    name                : 'Some new course',
                    university_course_id: '75.41',
                    university          : 'UBA',
                    description         : 'An awesome description',
                    semester            : "2019-2c",
                    semester_start_date : "2020-04-22",
                    semester_end_date   : "2020-08-25",
                    course_admin_id     : otherUser.getId()
            ]

        when: "post new course"
            def response = post("/api/courses", body, [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Course"
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map course = getJsonResponse(response)

            assert course.id != null
            assert course.name == body.name
            assert course.university_course_id == body.university_course_id
            assert course.description == body.description
            assert course.semester == body.semester

            assert courseUserRepository.findByCourse_IdAndUser_Id(course.id, otherUser.getId()).isPresent()

            assert courseRepository.existsById(course.id as Long)
    }


    @Unroll
    void "test create course with null values should not save course in DB"() {
        given: "a new course"
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    name                : name,
                    university_course_id: university_course_id,
                    university          : 'UBA',
                    description         : 'An awesome description',
                    semester            : semester,
                    course_admin_id     : user.getId(),
            ]

        when: "must fail with invalid request"
            def response = post("/api/courses", body, [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Course"
            response.contentType == "application/json"
            response.statusCode == SC_BAD_REQUEST

            Map result = getJsonResponse(response)

            assert result.message == "Invalid request"
            assert result.error == "validation_error"

        where:
            name              | university_course_id | semester
            null              | "75.41"              | '2019-2c'
            "Some new course" | null                 | '2019-2c'
            "Some new course" | '2019-2c'            | null
    }

    /*****************************************************************************************
     ********** COURSES **********************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get all courses should retrieve 1 course"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = get("/api/courses", [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 1
    }

    /*****************************************************************************************
     ********** COURSES BY USER **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get courses by user with enrolled user should retrieve 1 course"() {
        given:
            def profileResponse = getJsonResponse(get(
                    "/api/auth/profile",
                    username,
                    password
            ))

        when:
            def response = get(
                    String.format("/api/users/%s/courses", profileResponse.id),
                    username,
                    password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 1
    }

    @Unroll
    void "test get courses by user with wrong user id should not retrieve courses"() {
        when:
            def response = get(
                    "/api/users/22/courses",
                    username,
                    password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 0
    }

    @Unroll
    void "test get courses by user with unenrrolled user should not retrieve courses"() {
        given:
            def profileResponse = getJsonResponse(get(
                    "/api/auth/profile",
                    otherUsername,
                    otherPassword
            ))

        when:
            def response = get(
                    String.format("/api/users/%s/courses", profileResponse.id),
                    otherUsername,
                    otherPassword
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 0
    }

    /*****************************************************************************************
     ********** ENROLLL USER IN COURSE *******************************************************
     *****************************************************************************************/

    @Unroll
    void "test enroll user in courses"() {
        given:
            Map body = [username_or_email: otherUsername, password: otherPassword]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = post(String.format("/api/courses/%s/enroll", courseId), [], [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.name == 'student'
    }

    @Unroll
    void "test enroll user in wrong courses should fail with not found course"() {
        given:
            Map body = [username_or_email: otherUsername, password: otherPassword]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = post(String.format("/api/courses/%s/enroll", 22), [], [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)

            result.message == 'Course not found'
    }

    /*****************************************************************************************
     ********** UNENROLLL USER FROM COURSE ***************************************************
     *****************************************************************************************/

    @Unroll
    void "test unenroll user from courses"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = post(String.format("/api/courses/%s/unenroll", courseId), [], [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.statusCode == SC_NO_CONTENT
    }

    @Unroll
    void "test unenroll user in wrong courses should fail with not found course"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = post(String.format("/api/courses/%s/unenroll", 22), [], [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)

            result.message == 'Course not found'
    }

    /*****************************************************************************************
     ********** GET ALL COURSE USERS *********************************************************
     *****************************************************************************************/

    @Unroll
    void "test get users from course"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = get(String.format("/api/courses/%s/users", courseId), [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.size() == 1
    }

    @Unroll
    void "test get students from course"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = get(String.format("/api/courses/%s/users?roleName=student", courseId), [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.size() == 0
    }

    /*****************************************************************************************
     ********** UPDATE COURSE USER ***********************************************************
     *****************************************************************************************/

    @Unroll
    void "test update user from course"() {
        given:
            post(String.format("/api/courses/%s/enroll", courseId), [], otherUsername, otherPassword);

        when:
            def response = patch(String.format("/api/courses/%s/users/%s", courseId, otherUser.getId()), [
                    accepted: accepted,
                    role    : role
            ], username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            if (accepted) result.accepted == accepted
            if (role) result.role == role

        where:
            accepted | role
            true     | null
            false    | null
            null     | "student"
            null     | "admin"
            true     | "student"
            true     | "admin"
    }

    @Unroll
    void "test update user from course with wrong role should not update"() {
        given:
            post(String.format("/api/courses/%s/enroll", courseId), [], otherUsername, otherPassword);

        when:
            def response = patch(String.format("/api/courses/%s/users/%s", courseId, otherUser.getId()), [
                    role: "wrong-role"
            ], username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.role != "wrong-role"
    }

    @Unroll
    void "test update wrong user from course should fail with not found user"() {

        when:
            def response = patch(String.format("/api/courses/%s/users/%s", courseId, 22), [
                    role: "wrong-role"
            ], username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)

            result.message == 'Enrolled User not found in this Course'
    }

    /*****************************************************************************************
     ********** DELETE COURSE USER ***********************************************************
     *****************************************************************************************/

    @Unroll
    void "test delete user from course"() {
        given:
            post(String.format("/api/courses/%s/enroll", courseId), [], otherUsername, otherPassword);

        when:
            def response = delete(String.format("/api/courses/%s/users/%s", courseId, otherUser.getId()), username, password)

        then:
            response.statusCode == SC_NO_CONTENT
    }

    @Unroll
    void "test delete user in other course should fail with not found course"() {
        when:
            def response = delete(String.format("/api/courses/%s/users/%s", 22, 1), username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_FORBIDDEN

            def result = getJsonResponse(response)

            result.message == 'Forbidden'
    }

    @Unroll
    void "test delete missing user should fail with not found user"() {
        when:
            def response = delete(String.format("/api/courses/%s/users/%s", courseId, 22), username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)

            result.message == 'User not found'
    }

    @Unroll
    void "test delete missing user in course should fail with not found user"() {
        when:
            def response = delete(String.format("/api/courses/%s/users/%s", courseId, otherUser.getId()), username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)

            result.message == 'User not found in course'
    }

    /*****************************************************************************************
     ********** GET COURSE USER PERMISSIONS **************************************************
     *****************************************************************************************/

    @Unroll
    void "test get user course permissions"() {
        when:
            def response = get(String.format("/api/courses/%s/permissions", courseId), username, password);

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result == adminRole.getPermissions()
    }

    /*****************************************************************************************
     ********** GET COURSE SCOREBOARD ********************************************************
     *****************************************************************************************/

    @Unroll
    void "test get course scoreboard"() {
        given:
            courseUserRepository.save(new CourseUser(
                    course,
                    otherUser,
                    studentRole,
                    true
            ))
            ActivityCategory activityCategory = activityCategoryRepository.save(new ActivityCategory())
            RPLFile rplFile = fileRepository.save(new RPLFile())
            Activity activity = new Activity(
                    course,
                    activityCategory,
                    "Activity 1",
                    "An activity",
                    Language.C,
                    22,
                    rplFile,
                    "",
                    false
            )
            activityRepository.save(activity)

            submissionRepository.save(new ActivitySubmission(
                    activity,
                    otherUser,
                    rplFile,
                    SubmissionStatus.SUCCESS
            ))

        when:
            def response = get(
                    String.format("/api/courses/%s/scoreboard", courseId),
                    otherUsername,
                    otherPassword
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result[0].username == otherUsername
            result[0].score == activity.getPoints()
            result[0].activities_count == 1
    }
}
