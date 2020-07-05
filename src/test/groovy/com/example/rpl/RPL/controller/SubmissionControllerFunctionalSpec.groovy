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

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class SubmissionControllerFunctionalSpec extends AbstractFunctionalSpec {

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

    String username

    String password

    @Shared
    RPLFile submissionFile

    @Shared
    ActivitySubmission activitySubmission

    @Shared
    Course course


    def setup() {

        Role role = new Role(
                "admin",
                "course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage"
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
                "/somåe/uri"
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
                "starting_files",
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
                22,
                supportingActivityFile,
                "",
                false
        )
        activityRepository.save(activity)

        submissionFile = new RPLFile(
                "submission_file",
                "text",
                null
        )
        fileRepository.save(submissionFile)
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
     ********** GET SUBMISSION **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get submission should retrieve it"() {
        when:
            activitySubmission = new ActivitySubmission(
                    activity,
                    user,
                    submissionFile,
                    SubmissionStatus.PENDING
            )
            submissionRepository.save(activitySubmission)
            def response = get(String.format("/api/submissions/%s", activitySubmission.getId()), username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            assert result.id == activitySubmission.getId()
            assert result.submission_file_name == "submission_file"
            assert result.submission_file_type == "text"
            assert result.submission_file_id != null
            assert result.activity_starting_files_name == "starting_files"
            assert result.activity_starting_files_type == "text"
            assert result.activity_starting_files_id != null
            assert result.activity_language == "c_std11"
            assert result.activity_iotests.size() == 0
    }

    @Unroll
    void "test get non existent submission should respond with error"() {
        when:
            def response = get("/api/submissions/999")

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            def result = getJsonResponse(response)
            assert result.error == "activity_submission_not_found"
    }

    /*****************************************************************************************
     ********** POST SUBMISSION **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test POST submission should persist it and enqueue task"() {
        given: "A new submission"
            File f = new File("./src/main/resources/db/testdata/la_submission.tar.xz")

            Map loginBody = [username_or_email: "username", password: "supersecret"]
            def loginResponse = getJsonResponse(post("/api/auth/login", loginBody))

        when:
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("file", f)
            api.formParam("description", "This is my submission")
            api.contentType("multipart/form-data")
            def response = api.post("/api/courses/${course.getId()}/activities/${activity.getId()}/submissions")

        then:
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            def result = getJsonResponse(response)

            assert result.id != null
            assert result.submission_file_name != null
            assert result.submission_file_type == "application/gzip"
            assert result.submission_file_id != null
            assert result.activity_starting_files_name == "starting_files"
            assert result.activity_starting_files_type == "text"
            assert result.activity_starting_files_id != null
            assert result.activity_language == "c_std11"
            assert result.activity_iotests.size() == 0
    }
}


