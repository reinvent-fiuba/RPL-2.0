package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Shared
import spock.lang.Specification

class ActivitiesServiceSpec extends Specification {

    private ActivitiesService activitiesService;
    private CourseRepository courseRepository;
    private ActivityRepository activityRepository;
    private ActivityCategoryRepository activityCategoryRepository;
    private FileRepository fileRepository;
    private SubmissionRepository submissionRepository;

    @Shared
    private User user


    def setup() {
        courseRepository = Mock(CourseRepository)
        activityRepository = Mock(ActivityRepository)
        activityCategoryRepository = Mock(ActivityCategoryRepository)
        fileRepository = Mock(FileRepository)
        submissionRepository = Mock(SubmissionRepository)
        activitiesService = new ActivitiesService(
                courseRepository,
                activityRepository,
                activityCategoryRepository,
                fileRepository,
                submissionRepository
        )

        user = Mock(User)
        user.getId() >> 1
    }

    void "should create activity successfully"() {
        given:
            Long courseId = 1
            Long activityCategoryId = 1
            String name = "Some new activity"
            String description = "Some description"
            String language = "C"
            String initialCode = "//initial code"
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    initialCode,
                    22,
                    supportingFile.getBytes()
            )

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * activityCategoryRepository.findById(activityCategoryId) >> Optional.of(new ActivityCategory())
            1 * fileRepository.save(_ as RPLFile) >> { RPLFile file -> return file }
            1 * activityRepository.save(_ as Activity) >> { Activity activity -> return activity }

            assert newActivity.name == name
            assert newActivity.description == description
            assert newActivity.language == Language.getByName(language)
    }

    void "should fail to create activity if course not exists"() {
        given:
            Long courseId = 1
            Long activityCategoryId = 1
            String name = "Some new activity"
            String description = "Some description"
            String language = "C"
            String initialCode = "//initial code"
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    initialCode,
                    22,
                    supportingFile.getBytes()
            )

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should fail to create activity if activity category not exists"() {
        given:
            Long courseId = 1
            Long activityCategoryId = 1
            String name = "Some new activity"
            String description = "Some description"
            String language = "C"
            String initialCode = "//initial code"
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    initialCode,
                    22,
                    supportingFile.getBytes()
            )

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * activityCategoryRepository.findById(activityCategoryId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should get stats for user and course"() {
        given:
            Long courseId = 1
            Long userId = 1
            List<Activity> activities = []
            List<ActivitySubmission> submissions = []
            for (int i=0; i<totalActivities; i++) {
                activities.add(Mock(Activity))
            }
            for (int i=0; i<totalActivities*successSubmissionsByActivities; i++) {
                submissions.add(Mock(ActivitySubmission))
            }

        when:
            ActivitiesStats userStats = activitiesService.getActivitiesStatsByUserAndCourseId(userId, courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(Mock(Course))
            1 * activityRepository.findActivitiesByCourse(_ as Course) >> activities
            1 * submissionRepository.findAllByUserIdAndCourseId(userId, courseId) >> submissions
            for (int i=0; i<totalActivities*successSubmissionsByActivities; i++) {
                1 * submissions[i].getActivity() >> activities[i/successSubmissionsByActivities]
                submissions[i].getStatus() >> SubmissionStatus.SUCCESS
                1 * activities[i/successSubmissionsByActivities].getId() >> i/successSubmissionsByActivities
            }

            for (int i=0; i<totalActivities; i++) {
                1 * activities[i].getId() >> i
                1 * activities[i].getPoints() >> 22
            }

            userStats.getCountByStatus()["SOLVED"] == totalActivities
            userStats.getScore()["OBTAINED"] == 22*totalActivities
            userStats.getTotal() == totalActivities

        where:
            totalActivities | successSubmissionsByActivities
            1               | 1
            1               | 2
            2               | 2
    }
}
