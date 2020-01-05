package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.Activity
import com.example.rpl.RPL.model.ActivitySubmission
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.Language
import com.example.rpl.RPL.model.RPLFile
import com.example.rpl.RPL.model.SubmissionStatus
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.FileRepository
import com.example.rpl.RPL.repository.SubmissionRepository
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class SubmissionControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    CourseUserRepository courseUserRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ActivityRepository activityRepository

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

        userRepository.save(user)

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

        RPLFile supportingActivityFile = new RPLFile(
                "supporting_file",
                "text",
                null
        )
        fileRepository.save(supportingActivityFile)

        activity = new Activity(
                course,
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
                user.getId(),
                submissionFile,
                SubmissionStatus.PENDING
        )
        submissionRepository.save(activitySubmission)
    }

    def cleanup() {
        submissionRepository.deleteAll()
        activityRepository.deleteAll()
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
            def response = get(String.format("/api/submissions/%s", activitySubmission.getId()))

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)
            assert result.id == activitySubmission.getId()

            assert result.id == 1
            assert result.submission_file_name == "submission_file"
            assert result.submission_file_type == "text"
            assert result.submission_file_id == 2
            assert result.activity_supporting_file_name == "supporting_file"
            assert result.activity_supporting_file_type == "text"
            assert result.activity_supporting_file_id == 1
            assert result.activity_language == "c_std11"
            assert result.activity_iotests.size() == 0
    }
}


