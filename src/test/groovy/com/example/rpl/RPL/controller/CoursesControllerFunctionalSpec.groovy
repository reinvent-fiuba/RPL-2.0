package com.example.rpl.RPL.controller


import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.User

import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
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

        userRepository.save(otherUser)

        Course course = new Course(
            "some-course",
            "some-university-id",
            "some-description",
            true,
            "2019-2c",
            "/some/uri"
        )

        courseRepository.save(course);

        CourseUser courseUser = new CourseUser(
            course,
            user,
            null,
            true
        )

        courseUserRepository.save(courseUser);
    }

    def cleanup() {
        courseUserRepository.deleteAll()
        userRepository.deleteAll()
        courseRepository.deleteAll()
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
            System.out.println(result)
            result.size() == 1

        where:
            username    | password
            "username"  | "supersecret"
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

        where:
        username    | password
        "username"  | "supersecret"
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

        where:
        username    | password
        "username"  | "supersecret"
    }

    @Unroll
    void "test get courses by user with unenrrolled user should not retrieve courses"() {
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
        result.size() == 0

        where:
        username    | password
        "otheruser" | "supersecret"
    }
}


