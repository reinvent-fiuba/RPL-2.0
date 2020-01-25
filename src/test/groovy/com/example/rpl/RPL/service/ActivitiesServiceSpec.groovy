package com.example.rpl.RPL.service


import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.Activity
import com.example.rpl.RPL.model.ActivityCategory
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.Language
import com.example.rpl.RPL.model.RPLFile
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.ActivityCategoryRepository
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.FileRepository
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

    @Shared
    private User user


    def setup() {
        courseRepository = Mock(CourseRepository)
        activityRepository = Mock(ActivityRepository)
        activityCategoryRepository = Mock(ActivityCategoryRepository)
        fileRepository = Mock(FileRepository)
        activitiesService = new ActivitiesService(
                courseRepository,
                activityRepository,
                activityCategoryRepository,
                fileRepository
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
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    true, createActivityRequestDTO.getInitialCode(),
                    supportingFile
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
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    true, createActivityRequestDTO.getInitialCode(),
                    supportingFile
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
            MultipartFile supportingFile = new MockMultipartFile("some-name", "content".getBytes())

        when:
            Activity newActivity = activitiesService.createActivity(
                    courseId,
                    activityCategoryId,
                    name,
                    description,
                    language,
                    true, createActivityRequestDTO.getInitialCode(),
                    supportingFile
            )

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * activityCategoryRepository.findById(activityCategoryId) >> Optional.empty()

        thrown(NotFoundException)
    }
}
