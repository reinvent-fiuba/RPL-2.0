package com.example.rpl.RPL.controller.helpers

import com.example.rpl.RPL.model.CourseUser
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.ActivityCategoryRepository
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.CourseRepository
import com.example.rpl.RPL.repository.CourseUserRepository
import com.example.rpl.RPL.repository.FileRepository
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.SubmissionRepository
import com.example.rpl.RPL.repository.UserRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class StaticContextInizializer {

    @Autowired
    RoleRepository roleRepository
    @Autowired
    UserRepository userRepository
    @Autowired
    CourseRepository courseRepository
    @Autowired
    CourseUserRepository courseUserRepository
    @Autowired
    ActivityCategoryRepository activityCategoryRepository
    @Autowired
    ActivityRepository activityRepository
    @Autowired
    FileRepository fileRepository
    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    def init() {
        TestDataHelper.setRoleRepository(roleRepository)
        TestDataHelper.setUserRepository(userRepository)
        TestDataHelper.setCourseRepository(courseRepository)
        TestDataHelper.setCourseUserRepository(courseUserRepository)
        TestDataHelper.setActivityCategoryRepository(activityCategoryRepository)
        TestDataHelper.setActivityRepository(activityRepository)
        TestDataHelper.setFileRepository(fileRepository)
        TestDataHelper.setSubmissionRepository(submissionRepository)
        TestDataHelper.setPasswordEncoder(passwordEncoder)
    }
}
