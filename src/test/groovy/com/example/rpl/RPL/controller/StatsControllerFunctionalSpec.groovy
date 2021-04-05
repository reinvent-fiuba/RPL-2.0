package com.example.rpl.RPL.controller

import com.example.rpl.RPL.controller.helpers.TestDataHelper
import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.*
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import java.time.LocalDate

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class StatsControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired CourseUserRepository courseUserRepository
    @Autowired CourseRepository courseRepository
    @Autowired UserRepository userRepository
    @Autowired ActivityRepository activityRepository
    @Autowired ActivityCategoryRepository activityCategoryRepository;
    @Autowired SubmissionRepository submissionRepository
    @Autowired FileRepository fileRepository
    @Autowired RoleRepository roleRepository

    @Shared ActivityCategory activityCategory
    @Shared Activity activity
    @Shared User student
    @Shared User admin

    String username

    String password

    @Shared
    Course course

    def setup() {
        password = 'supersecret'
        student = TestDataHelper.createTestUser('student', password)
        admin = TestDataHelper.createTestUser('admin', password)
        username = admin.getUsername()
        course = TestDataHelper.createTestCourse()
        TestDataHelper.createTestCourseUser(course, admin, "admin")
        TestDataHelper.createTestCourseUser(course, student, "student")
        activityCategory = TestDataHelper.createTestActivityCategory(course)
        activity = TestDataHelper.createTestActivity(activityCategory);
        TestDataHelper.createTestSubmission(activity, student, SubmissionStatus.BUILD_ERROR)
        TestDataHelper.createTestSubmission(activity, student, SubmissionStatus.SUCCESS)
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
     ********** GET STATS BY ACTIVITY ********************************************************
     *****************************************************************************************/

    @Unroll
    void "test get stats group by activity should retrieve activity stat"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=activity", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 2
            stats.success == 1
            stats.build_error == 1
            stats.total_students == 1

            metadata.id == activity.getId().toString()
    }

    @Unroll
    void "test get stats group by activity filtering by all fields should retrieve activity stat"() {
        when:
        def response = get(
                String.format(
                        "/api/stats/courses/%s/submissions?groupBy=activity&categoryId=%s&userId=%s&activityId=%s&date=%s",
                        course.getId(),
                        activityCategory.getId(),
                        student.getId(),
                        activity.getId(),
                        LocalDate.now().toString()
                ),
                username, password
        )

        then:
        response.contentType == "application/json"
        response.statusCode == SC_OK

        def result = getJsonResponse(response)

        def stats = result.submissions_stats[0]
        def metadata = result.metadata[0]

        stats.total == 2
        stats.success == 1
        stats.build_error == 1
        stats.total_students == 1

        metadata.id == activity.getId().toString()
    }

    @Unroll
    void "test get stats group by activity filtering by different user should not find any submission"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=activity&userId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 0
            stats.success == 0
            stats.build_error == 0
            stats.total_students == 0

            metadata.id == activity.getId().toString()
    }

    @Unroll
    void "test get stats group by activity filtering by different date should not find any submission"() {
        when:
        def response = get(
                String.format("/api/stats/courses/%s/submissions?groupBy=activity&date=1995-09-22", course.getId()),
                username, password
        )

        then:
        response.contentType == "application/json"
        response.statusCode == SC_OK

        def result = getJsonResponse(response)

        def stats = result.submissions_stats[0]
        def metadata = result.metadata[0]

        stats.total == 0
        stats.success == 0
        stats.build_error == 0
        stats.total_students == 0

        metadata.id == activity.getId().toString()
    }

    @Unroll
    void "test get stats group by activity filtering by different activity category should not retrieve any activity"() {
        when:
        def response = get(
                String.format("/api/stats/courses/%s/submissions?groupBy=activity&categoryId=22", course.getId()),
                username, password
        )

        then:
        response.contentType == "application/json"
        response.statusCode == SC_OK

        def result = getJsonResponse(response)

        result.submissions_stats.size() == 0
        result.metadata.size() == 0
    }

    @Unroll
    void "test get stats group by activity filtering by different activity should fail with 404"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=activity&activityId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND
    }

    /*****************************************************************************************
     ********** GET STATS BY USER ************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get stats group by user should retrieve activity stat"() {
        when:
        def response = get(
                String.format("/api/stats/courses/%s/submissions?groupBy=user", course.getId()),
                username, password
        )

        then:
        response.contentType == "application/json"
        response.statusCode == SC_OK

        def result = getJsonResponse(response)

        def stats = result.submissions_stats[0]
        def metadata = result.metadata[0]

        stats.total == 2
        stats.success == 1
        stats.build_error == 1
        stats.total_students == 1

        metadata.id == student.getId().toString()
    }

    @Unroll
    void "test get stats group by user filtering by all fields should retrieve activity stat"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=user&categoryId=%s&userId=%s&activityId=%s&date=%s",
                            course.getId(),
                            activityCategory.getId(),
                            student.getId(),
                            activity.getId(),
                            LocalDate.now().toString()
                    ),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 2
            stats.success == 1
            stats.build_error == 1
            stats.total_students == 1

            metadata.id == student.getId().toString()
    }

    @Unroll
    void "test get stats group by user filtering by different user should fail with 404"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=user&userId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND
    }

    @Unroll
    void "test get stats group by user filtering by different date should not find any submission"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=user&date=1995-09-22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 0
            stats.success == 0
            stats.build_error == 0
            stats.total_students == 0

            metadata.id == student.getId().toString()
    }

    @Unroll
    void "test get stats group by user filtering by different activity category should not find any submission"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=user&categoryId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 0
            stats.success == 0
            stats.build_error == 0
            stats.total_students == 0

            metadata.id == student.getId().toString()
    }

    @Unroll
    void "test get stats group by user filtering by different activity should not retrieve any activity"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=user&activityId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 0
            stats.success == 0
            stats.build_error == 0
            stats.total_students == 0

            metadata.id == student.getId().toString()
    }

    /*****************************************************************************************
     ********** GET STATS BY DATE ************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get stats group by date should retrieve activity stat"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 2
            stats.success == 1
            stats.build_error == 1
            stats.total_students == 1

            metadata.date == LocalDate.now().toString()
    }

    @Unroll
    void "test get stats group by date filtering by all fields should retrieve activity stat"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date&categoryId=%s&userId=%s&activityId=%s&date=%s",
                            course.getId(),
                            activityCategory.getId(),
                            student.getId(),
                            activity.getId(),
                            LocalDate.now().toString()
                    ),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            def stats = result.submissions_stats[0]
            def metadata = result.metadata[0]

            stats.total == 2
            stats.success == 1
            stats.build_error == 1
            stats.total_students == 1

            metadata.date == LocalDate.now().toString()
    }

    @Unroll
    void "test get stats group by date filtering by different user should not find any user"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date&userId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND
    }

    @Unroll
    void "test get stats group by date filtering by different date should not retrieve any date"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date&date=1995-09-22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.submissions_stats.size() == 0
            result.metadata.size() == 0
    }

    @Unroll
    void "test get stats group by date filtering by different activity category should not retrieve any date"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date&categoryId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.submissions_stats.size() == 0
            result.metadata.size() == 0
    }

    @Unroll
    void "test get stats group by date filtering by different activity should not retrieve any date"() {
        when:
            def response = get(
                    String.format("/api/stats/courses/%s/submissions?groupBy=date&activityId=22", course.getId()),
                    username, password
            )

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.submissions_stats.size() == 0
            result.metadata.size() == 0
        }
}
