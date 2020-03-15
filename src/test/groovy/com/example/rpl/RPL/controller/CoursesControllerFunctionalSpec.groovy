package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.Role
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll

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
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    String username;

    String password;

    String otherUsername;

    String otherPassword;

    Long courseId;

    def setup() {
        User user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                passwordEncoder.encode('supersecret'),
                'some-university',
                'some-hard-degree'
        )

        username = 'username'
        password = 'supersecret'

        userRepository.save(user)

        User otherUser = new User(
                'other-name',
                'other-surname',
                'other-student-id',
                'otheruser',
                'other@mail.com',
                passwordEncoder.encode('supersecret'),
                'other-university',
                'other-hard-degree'
        )

        otherUsername = 'otheruser'
        otherPassword = 'supersecret'

        userRepository.save(otherUser)

        Course course = new Course(
                "some-course",
                "some-university-id",
                "some-description",
                true,
                "2019-2c",
                "/some/uri"
        )

        course = courseRepository.save(course);

        courseId = course.getId()

        Role adminRole = new Role('admin', 'course_admin')

        roleRepository.save(adminRole);

        Role studentRole = new Role('student', 'submit_activity')

        roleRepository.save(studentRole);

        CourseUser courseUser = new CourseUser(
                course,
                user,
                studentRole,
                true
        )

        courseUserRepository.save(courseUser)
    }

    def cleanup() {
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
            Map body = [usernameOrEmail: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    name              : 'Some new course',
                    universityCourseId: '75.41',
                    university        : 'UBA',
                    description       : 'An awesome description',
                    semester          : "2019-2c",
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
            assert course.university_course_id == body.universityCourseId
            assert course.description == body.description
            assert course.semester == body.semester

            assert courseRepository.existsById(course.id as Long)
    }

    @Unroll
    void "test create course with null values should not save course in DB"() {
        given: "a new course"
            Map body = [usernameOrEmail: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    name              : name,
                    universityCourseId: universityCourseId,
                    university        : 'UBA',
                    description       : 'An awesome description',
                    semester          : semester,
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
            name              | universityCourseId | semester
            null              | "75.41"            | '2019-2c'
            "Some new course" | null               | '2019-2c'
            "Some new course" | '2019-2c'          | null
    }

    /*****************************************************************************************
     ********** COURSES **********************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get all courses should retrieve 1 course"() {
        given:
            Map body = [usernameOrEmail: username, password: password]
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
            Map body = [usernameOrEmail: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))
            def profileResponse = getJsonResponse(get("/api/auth/profile", [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ]))

        when:
            def response = get(String.format("/api/users/%s/courses", profileResponse.id), [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 1
    }

    @Unroll
    void "test get courses by user with wrong user id should not retrieve courses"() {
        given:
            Map body = [usernameOrEmail: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = get("/api/users/3/courses", [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 0
    }

    @Unroll
    void "test get courses by user with unenrrolled user should not retrieve courses"() {
        given:
            Map body = [usernameOrEmail: 'otheruser', password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))
            def profileResponse = getJsonResponse(get("/api/auth/profile", [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ]))

        when:
            def response = get(String.format("/api/users/%s/courses", profileResponse.id), [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

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
            Map body = [usernameOrEmail: otherUsername, password: otherPassword]
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
            Map body = [usernameOrEmail: otherUsername, password: otherPassword]
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
            Map body = [usernameOrEmail: username, password: password]
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
            Map body = [usernameOrEmail: username, password: password]
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
}


