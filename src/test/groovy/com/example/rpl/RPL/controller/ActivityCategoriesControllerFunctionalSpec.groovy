package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.*
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import java.time.ZonedDateTime

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN
import static jakarta.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class ActivityCategoriesControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    CourseUserRepository courseUserRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    @Autowired
    ActivityCategoryRepository activityCategoryRepository;

    @Shared
    Course course

    @Shared
    def username

    @Shared
    def password

    def setup() {
        Role role = roleRepository.findByName("admin").get()

        User user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                passwordEncoder.encode('supersecret'),
                'some-university',
                'some-hard-degree',
        )
        user.markAsValidated()

        username = 'username'
        password = 'supersecret'

        userRepository.save(user)

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

        CourseUser courseUser = new CourseUser(
                course,
                user,
                role,
                true
        )

        courseUserRepository.save(courseUser);

        ActivityCategory activityCategory = new ActivityCategory(
                course,
                "Easy activities",
                "Some easy activities",
                true
        )

        activityCategoryRepository.save(activityCategory)
    }

    def cleanup() {
        activityCategoryRepository.deleteAll()
        courseUserRepository.deleteAll()
        userRepository.deleteAll()
        courseRepository.deleteAll()
    }

    /*****************************************************************************************
     ********** GET ACTIVITY CATEGORIES ******************************************************
     *****************************************************************************************/

    @Unroll
    void "test get activity categories should get all the categories"() {
        given:
            Long courseId = course.getId();
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when: "get activity categories"
            def response = get(String.format("/api/courses/%d/activityCategories", courseId), [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            result.size() == 1
    }

    @Unroll
    void "test post activity category should create a category"() {
        given:
            Long courseId = course.getId();
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))
            body = [
                    name: 'some-name',
                    description: 'some-description'
            ]

        when: "get activity categories"
            def response = post(String.format("/api/courses/%d/activityCategories", courseId), body, [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.name == 'some-name'
            result.description == 'some-description'
    }

    @Unroll
    void "test post activity category in other course should fail"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))
            body = [
                    name: 'some-name',
                    description: 'some-description'
        ]

        when: "get activity categories"
            def response = post(String.format("/api/courses/%d/activityCategories", course.getId()+1), body, [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then: "must return a new saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_FORBIDDEN

            def result = getJsonResponse(response)

            result.message == 'Forbidden'
    }
}
