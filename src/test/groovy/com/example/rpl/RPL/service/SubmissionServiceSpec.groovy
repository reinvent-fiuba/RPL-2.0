package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.BadRequestException
import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.ActivityRepository
import com.example.rpl.RPL.repository.FileRepository
import com.example.rpl.RPL.repository.SubmissionRepository
import com.example.rpl.RPL.repository.TestRunRepository
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Shared
import spock.lang.Specification

class SubmissionServiceSpec extends Specification {
    private ActivityRepository activityRepository
    private SubmissionRepository submissionRepository
    private FileRepository fileRepository
    private SubmissionService submissionService
    private TestService testService
    private TestRunRepository testRunRepository

    @Shared
    User user

    def setup() {
        activityRepository = Mock(ActivityRepository)
        submissionRepository = Mock(SubmissionRepository)
        fileRepository = Mock(FileRepository)
        testService = Mock(TestService)
        testRunRepository = Mock(TestRunRepository)
        submissionService = new SubmissionService(testService, activityRepository, submissionRepository, fileRepository, testRunRepository)

        user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                'supersecret',
                'some-university',
                'some-hard-degree'
        )
    }

    void "should throw NotFoundException when there is no submission"() {
        given: "no submissions"
            submissionRepository.findById(_ as Long) >> Optional.empty()

        when: "retrieving a submission"
            submissionService.getActivitySubmission(1)

        then:
            NotFoundException e = thrown(NotFoundException)
            assert e.getError() == "activity_submission_not_found"
    }

    void "should return a submission"() {
        given: "1 submission"
            RPLFile f = new RPLFile("test_file", "text", null)

            ActivitySubmission activitySubmission = new ActivitySubmission(null, user, f,
                    SubmissionStatus.PENDING)

            submissionRepository.findById(_ as Long) >> Optional.of(activitySubmission)

        when: "retrieving a submission"
            ActivitySubmission aSub = submissionService.getActivitySubmission(1)

        then:
            assert aSub == activitySubmission
    }

    void "should create a submission"() {
        given: "an activity"
            Activity a = new Activity()
            activityRepository.findById(_ as Long) >> Optional.of(a)

        when: "creating a submission"
            ActivitySubmission aSub = submissionService.create(user, 1, 1, "bla", new MockMultipartFile("mockfile"))

        then:
            1 * fileRepository.save(_ as RPLFile) >> { RPLFile f -> return f }
            1 * submissionRepository.save(_ as ActivitySubmission) >> { ActivitySubmission sub -> return sub }

            assert aSub.getActivity() == a
            assert aSub.getStatus() == SubmissionStatus.PENDING
            assert aSub.getUser() == user
    }

    void "createSubmission should throw NotFoundException when there is no activity"() {
        given: "no activity"
            activityRepository.findById(_ as Long) >> Optional.empty()

        when: "creating a submission"
            submissionService.create(user, 1, 1, "bla", new MockMultipartFile("mockfile"))

        then:
            NotFoundException e = thrown(NotFoundException)
            assert e.getError() == "activity_not_found"
    }

    void "createSubmission should throw BadRequestException when MultipartFile is corrupted"() {
        given: "an activity"
            Activity a = new Activity()
            activityRepository.findById(_ as Long) >> Optional.of(a)

        and: "a corrupted MultiPartFile"
            MultipartFile f = Mock(MultipartFile)
            f.getBytes() >> { throw new IOException() }


        when: "creating a submission"
            submissionService.create(user, 1, 1, "bla", f)

        then:
            BadRequestException e = thrown(BadRequestException)
            assert e.getError() == "bad_file"
    }


}
