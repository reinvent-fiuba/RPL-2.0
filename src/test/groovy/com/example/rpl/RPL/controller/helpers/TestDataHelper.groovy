package com.example.rpl.RPL.controller.helpers

import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.*
import lombok.Setter
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.ZonedDateTime

@Setter
class TestDataHelper {

    static RoleRepository roleRepository
    static UserRepository userRepository
    static CourseRepository courseRepository
    static CourseUserRepository courseUserRepository
    static ActivityCategoryRepository activityCategoryRepository
    static ActivityRepository activityRepository
    static FileRepository fileRepository
    static SubmissionRepository submissionRepository

    static PasswordEncoder passwordEncoder

    static def createTestCourse() {
        Course course = new Course(
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

        return courseRepository.save(course);
    }

    static def createTestUser(String username, String password) {
        User user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                username,
                String.format('mail-%s@mail.com', new Date().getTime()),
                passwordEncoder.encode(password),
                'some-university',
                'some-hard-degree'
        )
        user.markAsValidated()
        return userRepository.save(user)
    }

    static def createTestCourseUser(Course course, User user, String roleName) {
        Role role = roleRepository.findByName(roleName).get()

        CourseUser courseUser = new CourseUser(
                course,
                user,
                role,
                true
        )
        return courseUserRepository.save(courseUser);
    }

    static def createTestActivityCategory(Course course) {
        ActivityCategory activityCategory = new ActivityCategory(
                course,
                "Easy activities",
                "Some easy activities",
                true
        )

        return activityCategoryRepository.save(activityCategory)
    }

    static def createTestActivity(ActivityCategory activityCategory) {
        File f = new File("./src/main/resources/data_for_tests_do_not_delete/activity_1_starting_files/activity_1_starting_files.gz")

        RPLFile supportingActivityFile = new RPLFile(
                "starting_files",
                "text",
                f.getBytes()
        )
        fileRepository.save(supportingActivityFile)

        Activity activity = new Activity(
                activityCategory.getCourse(),
                activityCategory,
                "Activity 1",
                "An activity",
                Language.C,
                22,
                supportingActivityFile,
                "",
                true
        )
        return activityRepository.save(activity)
    }

    static def createTestSubmission(Activity activity, User user, SubmissionStatus status) {
        RPLFile submissionFile = new RPLFile(
                "starting_files",
                "text",
                null
        )
        fileRepository.save(submissionFile)

        ActivitySubmission activitySubmission = new ActivitySubmission(
                activity,
                user,
                submissionFile,
                status
        )
        return submissionRepository.save(activitySubmission)
    }
}
