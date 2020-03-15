package com.example.rpl.RPL.service


import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.ActivityCategoryRepository
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.FileRepository
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Shared
import spock.lang.Specification

class ActivityCategoriesServiceSpec extends Specification {

    private  ActivityCategoriesService activityCategoriesService;
    private CourseRepository courseRepository;
    private ActivityCategoryRepository activityCategoryRepository;

    @Shared
    private User user


    def setup() {
        courseRepository = Mock(CourseRepository)
        activityCategoryRepository = Mock(ActivityCategoryRepository)
        activityCategoriesService = new ActivityCategoriesService(
                courseRepository,
                activityCategoryRepository
        )

        user = Mock(User)
        user.getId() >> 1
    }

    void "should retrieve all activity categories successfully"() {
        given:
            Long courseId = 1

        when:
            List<ActivityCategory> activityCategories = activityCategoriesService.getActivityCategories(courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * activityCategoryRepository.findByCourse_Id(_ as Long) >> [ new ActivityCategory() ]

            activityCategories.size() == 1
    }

    void "should fail to retrieve all activity categories successfully"() {
        given:
            Long courseId = 1

        when:
            List<ActivityCategory> activityCategories = activityCategoriesService.getActivityCategories(courseId)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()

            thrown(NotFoundException)
    }

    void "should create activity category successfully" () {
        given:
            Long courseId = 1
            String name = "Some category"
            String description = "Some description"

        when:
            ActivityCategory newActivityCategory = activityCategoriesService.createActivityCategory(courseId, name, description)

        then:
            1 * courseRepository.findById(courseId) >> Optional.of(new Course())
            1 * activityCategoryRepository.save(_ as ActivityCategory) >> { ActivityCategory activityCategory -> return activityCategory }

        assert newActivityCategory.name == name
        assert newActivityCategory.description == description
    }

    void "should fail to create activity category if course not exist" () {
        given:
            Long courseId = 1
            String name = "Some category"
            String description = "Some description"

        when:
            ActivityCategory newActivityCategory = activityCategoriesService.createActivityCategory(courseId, name, description)

        then:
            1 * courseRepository.findById(courseId) >> Optional.empty()

            thrown(NotFoundException)
    }
}
