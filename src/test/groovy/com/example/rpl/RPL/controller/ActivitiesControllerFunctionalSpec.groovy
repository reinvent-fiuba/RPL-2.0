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

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class ActivitiesControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    CourseUserRepository courseUserRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ActivityRepository activityRepository

    @Autowired
    ActivityCategoryRepository activityCategoryRepository;

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    FileRepository fileRepository

    @Shared
    Activity activity

    @Shared
    User user

    @Shared
    RPLFile submissionFile

    @Shared
    ActivitySubmission activitySubmission

    @Shared
    Course course

    String username;

    String password;

    def setup() {
        Role role = new Role(
                "student",
                "activity_submit"
        )
        roleRepository.save(role);

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

        username = 'username'
        password = 'supersecret'

        userRepository.save(user)

        course = new Course(
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
                role,
                true
        )

        courseUserRepository.save(courseUser);

        RPLFile supportingActivityFile = new RPLFile(
                "supporting_file",
                "text",
                null
        )
        fileRepository.save(supportingActivityFile)

        ActivityCategory activityCategory = new ActivityCategory(
                course,
                "Easy activities",
                "Some easy activities",
                true
        )

        activityCategoryRepository.save(activityCategory)

        activity = new Activity(
                course,
                activityCategory,
                "Activity 1",
                "An activity",
                Language.C,
                supportingActivityFile
        )
        activityRepository.save(activity)

        submissionFile = new RPLFile(
                "submission_file",
                "text",
                null
        )
        fileRepository.save(submissionFile)

        activitySubmission = new ActivitySubmission(
                activity,
                user,
                submissionFile,
                SubmissionStatus.PENDING
        )
        submissionRepository.save(activitySubmission)

    }

    def cleanup() {
        submissionRepository.deleteAll()
        activityRepository.deleteAll()
        activityCategoryRepository.deleteAll()
        fileRepository.deleteAll()
        courseUserRepository.deleteAll()
        userRepository.deleteAll()
        courseRepository.deleteAll()
    }

    /*****************************************************************************************
     ********** CREATE ACTIVITY **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test create activity with correct values should save activity in DB"() {
        given: "a new activity"
            Long courseId = 1;
            Map body = [usernameOrEmail: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId:  1,
                    name:               'Some name',
                    description:        'Some description',
                    language:           'C'
            ]

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", courseId))

        then: "must return a new saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map activity = getJsonResponse(response)

            assert activity.id != null
            assert activity.name == "Some name"
            assert activity.description == "Some description"

            assert activityRepository.existsById(activity.id as Long)
            assert fileRepository.existsById(activity.file_id as Long)
    }

    @Unroll
    void "test create course with null values should not save course in DB"() {
        given: "a new course"
            Long courseId = 1;
            Map body = [usernameOrEmail: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategoryId,
                    name:               name,
                    description:        'Some description',
                    language:           language
            ].findAll{ it.value!=null }

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", courseId))

        then: "must return a new saved Course"
            response.contentType == "application/json"
            response.statusCode == SC_BAD_REQUEST

            Map result = getJsonResponse(response)

            assert result.message == "Invalid request"
            assert result.error == "validation_error"

        where:
            activityCategoryId | name        | language
            null               | "Some name" | 'C'
            1                  | null        | 'C'
            1                  | "Some name" | null

    }


}


