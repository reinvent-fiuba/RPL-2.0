package com.example.rpl.RPL.service


import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import spock.lang.Specification

class CoursesServiceSpec extends Specification {

    private CoursesService coursesService
    private CourseUserRepository courseUserRepository
    private CourseRepository courseRepository

    def setup() {
        courseUserRepository = Mock(CourseUserRepository)
        courseRepository = Mock(CourseRepository)
        coursesService = new CoursesService(courseRepository, courseUserRepository, null, null)
    }

    void "should return an empty list of courses when calling getAllCourses"() {
        given: "no courses"
        courseRepository.findAll() >> []

        when: "retrieving all courses"
            List< Course> courses = coursesService.getAllCourses()

        then: "there are no courses"
            courses.isEmpty()
    }

    void "should return some courses when calling getAllCourses"() {
        given: "1 course"
        courseRepository.findAll() >> [
                new Course()
        ]

        when: "retrieving all course"
        List< Course> courses = coursesService.getAllCourses()

        then: "there are no courses"
        courses.size() == 1
    }

    void "should return an empty list of courses when calling getAllCoursesByUser"() {
        given: "no course users"
        courseUserRepository.findByUser_Id(1) >> []

        when: "retrieving user courses"
        List< Course> courses = coursesService.getAllCoursesByUser(1)

        then: "there are no courses"
        courses.isEmpty()
    }

    void "should return some courses when calling getAllCoursesByUser"() {
        given: "1 course"
        Course course = Mock(Course)
        CourseUser courseUser = Mock(CourseUser)
        courseUser.getCourse() >> course
        courseUserRepository.findByUser_Id(1) >> [
                courseUser
        ]

        when: "retrieving all courses"
        List< Course> courses = coursesService.getAllCoursesByUser(1)

        then: "there are no courses"
        courses.size() == 1
    }
}
