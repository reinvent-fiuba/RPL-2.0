package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.*
import com.example.rpl.RPL.repository.*
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class ActivitiesControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    CourseUserRepository courseUserRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ActivityRepository activityRepository

    @Autowired
    ActivityCategoryRepository activityCategoryRepository;

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    FileRepository fileRepository

    @Autowired
    IOTestRepository iOTestRepository;

    @Shared
    Activity activity

    @Shared
    User user

    @Shared
    RPLFile supportingActivityFile

    @Shared
    RPLFile submissionFile

    @Shared
    Course course

    @Shared
    ActivityCategory activityCategory

    String username;

    String password;

    def setup() {
        Role role = new Role(
                "admin",
                "course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage"
        )
        roleRepository.save(role);

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

        username = 'username'
        password = 'supersecret'

        userRepository.save(user)

        course = new Course(
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
                role,
                true
        )

        courseUserRepository.save(courseUser);

        supportingActivityFile = new RPLFile(
                "supporting_file",
                "text",
                null
        )
        fileRepository.save(supportingActivityFile)

        activityCategory = new ActivityCategory(
                course,
                "Easy activities",
                "Some easy activities",
                true
        )

        activityCategory = activityCategoryRepository.save(activityCategory)

        activity = new Activity(
                course,
                activityCategory,
                "Activity 1",
                "An activity",
                Language.C,
                "initialCode",
                10,
                supportingActivityFile
        )
        activityRepository.save(activity)

        submissionFile = new RPLFile(
                "submission_file",
                "text",
                null
        )
        fileRepository.save(submissionFile)
    }

    def cleanup() {
        iOTestRepository.deleteAll()
        submissionRepository.deleteAll()
        activityRepository.deleteAll()
        activityCategoryRepository.deleteAll()
        fileRepository.deleteAll()
        courseUserRepository.deleteAll()
        userRepository.deleteAll()
        courseRepository.deleteAll()
        roleRepository.deleteAll()
    }

    /*****************************************************************************************
     ********** CREATE ACTIVITY **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test create activity with correct values should save activity in DB"() {
        given: "a new activity"
            Long courseId = course.getId()
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategory.getId(),
                    name              : 'Some name',
                    description       : 'Some description',
                    language          : 'C',
                    initialCode       : '//initial code',
                    points            : 22,
            ]

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", courseId))

        then: "must return a new saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map activity = getJsonResponse(response)

            assert activity.id != null
            assert activity.name == "Some name"
            assert activity.description == "Some description"

            assert activityRepository.existsById(activity.id as Long)
            assert fileRepository.existsById(activity.file_id as Long)
    }

    @Unroll
    void "test create activity with null values should not save activity in DB"() {
        given: "a new activity"
            Long courseId = 1;
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategoryId,
                    name              : name,
                    description       : 'Some description',
                    language          : language,
                    initialCode       : '//initial code'
            ].findAll { it.value != null }

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", courseId))

        then: "must fail with Bad Request error"
            response.contentType == "application/json"
            response.statusCode == SC_BAD_REQUEST

            Map result = getJsonResponse(response)

            assert result.message == "Invalid request"
            assert result.error == "validation_error"

        where:
            activityCategoryId | name        | language
            null               | "Some name" | 'C'
            1                  | null        | 'C'
            1                  | "Some name" | null
    }

    @Unroll
    void "test create activity in other course should not save activity in DB"() {
        given: "a new activity"
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategory.getId(),
                    name              : 'Some name',
                    description       : 'Some description',
                    language          : 'C',
                    initialCode       : '//initial code'
            ].findAll { it.value != null }

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", course.getId() + 1))

        then: "must fail with Forbidden Error"
            response.contentType == "application/json"
            response.statusCode == SC_FORBIDDEN

            Map result = getJsonResponse(response)
            assert result.message == "Forbidden"
            assert result.error == "forbidden"
    }

    @Unroll
    void "test create activity with wrong activityCategoryId should not save activity in DB"() {
        given: "a new activity"
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategory.getId() + 1,
                    name              : 'Some name',
                    description       : 'Some description',
                    language          : 'C',
                    initialCode       : '//initial code'
            ].findAll { it.value != null }

        when: "post new activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = api.post(String.format("/api/courses/%d/activities", course.getId()))

        then: "must fail with Not Found Error"
            response.contentType == "application/json"
            response.statusCode == SC_NOT_FOUND

            Map result = getJsonResponse(response)
            assert result.message == "Category not found"
            assert result.error == "category_not_found"
    }

    /*****************************************************************************************
     ********** UPDATE ACTIVITY **************************************************************
     *****************************************************************************************/

    @Unroll
    void "test update activity with correct values should update activity in DB"() {
        given: "a new activity"
            Long courseId = course.getId()
            Long activityId = activity.getId()
            Map body = [username_or_email: username, password: password]
            File f = new File("./src/main/resources/db/testdata/unit_test_google.c")
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

            body = [
                    activityCategoryId: activityCategory.getId(),
                    name              : 'Some NEW name',
                    description       : 'Some NEWWWWWW description',
                    language          : activity.getLanguage().getName(),
                    initialCode       : '//another initial code'
            ].findAll { it.value != null }

        when: "put modified activity"
            api.headers([
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])
            api.multiPart("supportingFile", f)
            api.formParams(body)
            api.contentType("multipart/form-data")
            def response = put("/api/courses/${courseId}/activities/${activityId}", null)

        then: "must return a modified saved Activity"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map modifiedActivity = getJsonResponse(response)

            assert modifiedActivity.id == activity.getId()
            assert modifiedActivity.name == "Some NEW name"
            assert modifiedActivity.description == "Some NEWWWWWW description"
            assert modifiedActivity.initial_code == "//another initial code"

            assert activityRepository.existsById(modifiedActivity.id as Long)
            assert fileRepository.existsById(modifiedActivity.file_id as Long)
    }

    /*****************************************************************************************
     ********** GET ACTIVITIES ***************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get activity should return the activity"() {
        when:
            def response = get("/api/courses/${course.getId()}/activities/${activity.getId()}", username, password)

        then:
            Map result = getJsonResponse(response)
            assert result.id == activity.id
            assert result.course_id == course.getId()
            assert result.category_name == activityCategory.getName()
            assert result.category_description == activityCategory.getDescription()
            assert result.name == activity.name
            assert result.description == activity.description
            assert result.language == activity.language.getName()
            assert result.active == activity.active
            assert result.initial_code == activity.initialCode
            assert result.file_id == activity.supportingFile.getId()
            assert result.activity_unit_tests == null
            assert result.activity_iotests == []
            assert result.date_created != null
            assert result.last_updated != null
    }

    @Unroll
    void "test get activity should return the activity with tests"() {
        given: "Two unit tests added to the activity"
            IOTest ioTest1 = iOTestRepository.save(new IOTest(activity.getId(), "1", "1"))
            IOTest ioTest2 = iOTestRepository.save(new IOTest(activity.getId(), "2", "2"))

        when:
            def response = get("/api/courses/${course.getId()}/activities/${activity.getId()}", username, password)

        then:
            Map result = getJsonResponse(response)
            assert result.id == activity.id
            assert result.course_id == course.getId()
            assert result.category_name == activityCategory.getName()
            assert result.category_description == activityCategory.getDescription()
            assert result.name == activity.name
            assert result.description == activity.description
            assert result.language == activity.language.getName()
            assert result.active == activity.active
            assert result.initial_code == activity.initialCode
            assert result.file_id == activity.supportingFile.getId()
            assert result.activity_unit_tests == null
            assert result.activity_iotests == [[id: ioTest1.getId(), in: "1", out: "1"], [id: ioTest2.getId(), in: "2", out: "2"]]
            assert result.date_created != null
            assert result.last_updated != null
    }

    @Unroll
    void "test get activities should return all the user's activities with submission results"() {
        given: "another activity without submissions"
            submissionRepository.save(new ActivitySubmission(
                    activity,
                    user,
                    submissionFile,
                    SubmissionStatus.PENDING
            ))
            Activity activity2 = new Activity(
                    course,
                    activityCategory,
                    "Activity 2",
                    "Another activity",
                    Language.PYTHON3,
                    "def hola():",
                    22,
                    supportingActivityFile
            )
            activityRepository.save(activity2)

        when:
            def response = get("/api/courses/${course.getId()}/activities", username, password)

        then:
            List result = getJsonResponse(response)
            assert result.size() == 2

            Map resultActivity1 = result[0]
            assert resultActivity1.id == activity.id
            assert resultActivity1.course_id == course.getId()
            assert resultActivity1.category_name == activityCategory.getName()
            assert resultActivity1.category_description == activityCategory.getDescription()
            assert resultActivity1.name == activity.name
            assert resultActivity1.description == activity.description
            assert resultActivity1.language == activity.language.name()
            assert resultActivity1.active == activity.active
            assert resultActivity1.file_id == activity.supportingFile.getId()
            assert resultActivity1.submission_status == "PENDING"
            assert resultActivity1.last_submission_date != null
            assert resultActivity1.date_created != null
            assert resultActivity1.last_updated != null

            Map resultActivity2 = result[1]
            assert resultActivity2.id == activity2.id
            assert resultActivity2.course_id == course.getId()
            assert resultActivity2.category_name == activityCategory.getName()
            assert resultActivity2.category_description == activityCategory.getDescription()
            assert resultActivity2.name == activity2.name
            assert resultActivity2.description == activity2.description
            assert resultActivity2.language == activity2.language.name()
            assert resultActivity2.active == activity2.active
            assert resultActivity2.file_id == activity2.supportingFile.getId()
            assert resultActivity2.submission_status == ""
            assert resultActivity2.last_submission_date == null
            assert resultActivity2.date_created != null
            assert resultActivity2.last_updated != null
    }

    /*****************************************************************************************
     ********** CREATE IO TEST ***************************************************************
     *****************************************************************************************/

    @Unroll
    void "test create IO test for activity"() {
        given: "a new IO Test"
            Map body = [text_in: "1", text_out: "2"]

        when:
            def response = post("/api/courses/${course.getId()}/activities/${activity.getId()}/iotests", body, username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map ioTest = getJsonResponse(response)
            assert ioTest.in == "1"
            assert ioTest.out == "2"
    }

    @Unroll
    void "test update IO test for activity"() {
        given: "Two unit tests added to the activity"
            IOTest ioTest1 = iOTestRepository.save(new IOTest(activity.getId(), "1", "1"))
            IOTest ioTest2 = iOTestRepository.save(new IOTest(activity.getId(), "2", "2"))

        and: "a change"
            Map body = [text_in: "1000", text_out: "2000"]

        when:
            def response = put("/api/courses/${course.getId()}/activities/${activity.getId()}/iotests/${ioTest1.getId()}", body, username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map ioTest = getJsonResponse(response)
            assert ioTest.in == "1000"
            assert ioTest.out == "2000"
    }

    @Unroll
    void "test delete IO test for activity"() {
        given: "Two unit tests added to the activity"
            IOTest ioTest1 = iOTestRepository.save(new IOTest(activity.getId(), "1", "1"))
            IOTest ioTest2 = iOTestRepository.save(new IOTest(activity.getId(), "2", "2"))

        when: "deleting one of those"
            def response = delete("/api/courses/${course.getId()}/activities/${activity.getId()}/iotests/${ioTest1.getId()}", username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map result = getJsonResponse(response)
            assert result.id == activity.id
            assert result.course_id == course.getId()
            assert result.category_name == activityCategory.getName()
            assert result.category_description == activityCategory.getDescription()
            assert result.name == activity.name
            assert result.description == activity.description
            assert result.language == activity.language.getName()
            assert result.active == activity.active
            assert result.initial_code == activity.initialCode
            assert result.file_id == activity.supportingFile.getId()
            assert result.activity_unit_tests == null
            assert result.activity_iotests == [[id: ioTest2.getId(), in: "2", out: "2"]]
            assert result.date_created != null
            assert result.last_updated != null

    }

    /*****************************************************************************************
     ********** GET ACTIVITIES STATS *********************************************************
     *****************************************************************************************/

    @Unroll
    void "test get activities stats"() {
        given:
            for (submissionStatus in submissionStatuses) {
                submissionRepository.save(new ActivitySubmission(
                        activity,
                        user,
                        submissionFile,
                        submissionStatus
                ))
            }
        when:
            def response = get("/api/courses/${course.getId()}/activities/stats", username, password);

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            def result = getJsonResponse(response)

            result.count_by_status == ["NON STARTED": nonStarted, "STARTED": started, "SOLVED": solved]
            result.score == ["OBTAINED": score, "PENDING": totalScore-score]

        where:
            submissionStatuses         | score | totalScore | started | solved | nonStarted
            []                         | 0     | 10         | 0       | 0      | 1
            [SubmissionStatus.PENDING] | 0     | 10         | 1       | 0      | 0
            [SubmissionStatus.SUCCESS] | 10    | 10         | 0       | 1      | 0
            [SubmissionStatus.PENDING,
             SubmissionStatus.SUCCESS] | 10    | 10         | 0       | 1      | 0
            [SubmissionStatus.SUCCESS,
             SubmissionStatus.SUCCESS] | 10    | 10         | 0       | 1      | 0
    }
}
