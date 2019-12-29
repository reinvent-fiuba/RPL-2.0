package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.model.Course
import com.example.rpl.RPL.model.CourseSemester
import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.CourseSemesterRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class CoursesServiceSpec extends Specification {

    private CoursesService coursesService
    private CourseUserRepository courseUserRepository
    private CourseSemesterRepository courseSemesterRepository

    def setup() {
        courseUserRepository = Mock(CourseUserRepository)
        courseSemesterRepository = Mock(CourseSemesterRepository)
        coursesService = new CoursesService(courseSemesterRepository, courseUserRepository)
    }

    void "should return an empty list of courseSemesters when calling getAllCoursesSemester"() {
        given: "no course semesters"
        courseSemesterRepository.findAll() >> []

        when: "retrieving all course semesters"
            List< CourseSemester> courseSemesters = coursesService.getAllCoursesSemester()

        then: "there are no course semesters"
            courseSemesters.isEmpty()
    }

    void "should return some courseSemesters when calling getAllCoursesSemester"() {
        given: "1 course semesters"
        courseSemesterRepository.findAll() >> [
                new CourseSemester()
        ]

        when: "retrieving all course semesters"
        List< CourseSemester> courseSemesters = coursesService.getAllCoursesSemester()

        then: "there are no course semesters"
        courseSemesters.size() == 1
    }

    void "should return an empty list of courseSemesters when calling getAllCoursesSemesterFromUser"() {
        given: "no course users"
        courseUserRepository.findByUser_Id(1) >> []

        when: "retrieving user course semesters"
        List< CourseSemester> courseSemesters = coursesService.getAllCoursesSemesterFromUser(1)

        then: "there are no course semesters"
        courseSemesters.isEmpty()
    }

    void "should return some courseSemesters when calling getAllCoursesSemesterFromUser"() {
        given: "1 course semesters"
        CourseSemester courseSemester = Mock(CourseSemester)
        CourseUser courseUser = Mock(CourseUser)
        courseUser.getCourseSemester() >> courseSemester
        courseUserRepository.findByUser_Id(1) >> [
                courseUser
        ]

        when: "retrieving all course semesters"
        List< CourseSemester> courseSemesters = coursesService.getAllCoursesSemesterFromUser(1)

        then: "there are no course semesters"
        courseSemesters.size() == 1
    }
}
