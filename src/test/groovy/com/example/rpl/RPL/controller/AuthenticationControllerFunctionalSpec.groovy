package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class AuthenticationControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    String username

    String password

    @Shared
    User user

    def setup() {
        user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                passwordEncoder.encode('supersecret'),
                'some-university',
                'some-hard-degree'
        );
        username = user.getUsername()
        password = "supersecret";

        user.markAsValidated()
        userRepository.save(user);
    }

    def cleanup() {
        userRepository.deleteAll();
    }

    /*****************************************************************************************
     ********** REGISTER NEW USER ************************************************************
     *****************************************************************************************/

    def "Create user with correct values must save user in DB"() {
        given: "a new user"
            def body = [
                    username  : username,
                    email     : email,
                    password  : password,
                    name      : "Ale",
                    surname   : "Levinas",
                    student_id: "95719",
                    degree    : "Ing. Informatica",
                    university: "UBA"
            ]

        when: "post new user"
            def response = post("/api/auth/signup", body)

        then: "must return a new saved User"
            response.contentType == "application/json"
            response.statusCode == SC_CREATED

            Map user = getJsonResponse(response)

            assert user.id != null
            assert user.username == body.username
            assert user.email == body.email
            assert user.password != body.password // as it is encrypted
            assert user.name == body.name
            assert user.surname == body.surname
            assert user.student_id == body.student_id
            assert user.degree == body.degree
            assert user.university == body.university

            assert user.email_validated == false
            assert user.date_created != null
            assert user.last_updated == user.date_created

            assert userRepository.existsById(user.id as Long)

        where:
            username   | email         | password
            "alep1234" | "asd@asd.com" | "12345"

    }

    @Unroll
    def "Create user with null values must fail"() {
        given: "a new user"
            def body = [
                    username  : username,
                    email     : email,
                    password  : password,
                    name      : "Ale",
                    surname   : "Levinas",
                    student_id: "95719",
                    degree    : "Ing. Informatica",
                    university: "UBA"
            ]

        when: "post new user"
            def response = post("/api/auth/signup", body)

        then: "must fail with invalid request"
            response.contentType == "application/json"
            response.statusCode == SC_BAD_REQUEST

            Map result = getJsonResponse(response)

            assert result.message == "Invalid request"
            assert result.error == "validation_error"

        where:
            username | email         | password
            null     | "asd@asd.com" | 1234
            "alepox" | null          | 1234
            "alepox" | "asd@asd.com" | null
    }

    @Unroll
    void "should fail hibernate validation #test creating user"() {
        given: "a new user"
            def body = [
                    username  : username,
                    email     : email,
                    password  : password,
                    name      : "Ale",
                    surname   : "Levinas",
                    student_id: "95719",
                    degree    : "Ing. Informatica",
                    university: "UBA"
            ]

        when:
            def response = post("/api/auth/signup", body)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_BAD_REQUEST

            Map result = getJsonResponse(response)

            assert result.message == "Invalid request"
            assert result.error == "validation_error"

        where:
            test                          | username        | email            | password
            "username less than 6 chars"  | "ale"           | "asd@asdd.com"   | 1234
            "username more than 12 chars" | "aleeeeeeeeeee" | "asd@asddd.com"  | 1234
            "invalid email"               | "alepoxx"       | "alegmail.com"   | 1234
            "invalid password"            | "alepoxxx"      | "asd@asdddd.com" | null
    }

    /*****************************************************************************************
     ********** LOG IN ***********************************************************************
     *****************************************************************************************/

    void "test wrong username and password should respond with Bad Credentials"() {
        given:
            String username = "ale"
            String password = "1"

            Map body = [username_or_email: username, password: password]

        when:
            def response = post("/api/auth/login", body)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_UNAUTHORIZED

            Map result = getJsonResponse(response)

            assert result.message == "Bad credentials"
            assert result.error == "bad_credentials"
    }

    @Unroll
    void "test login with correct username/email and password should respond with token"() {
        given:
            Map body = [username_or_email: usernameOrEmail, password: 'supersecret']

        when:
            def response = post("/api/auth/login", body)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map result = getJsonResponse(response)
            assert result.access_token != null
            assert result.token_type == "Bearer"

        where:
            usernameOrEmail << ['username', 'some@mail.com']
    }

    /*****************************************************************************************
     ********** PROFILE **********************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get current profile should retrieve user data"() {
        given:
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))

        when:
            def response = get("/api/auth/profile", [
                    "Authorization": String.format("%s %s", loginResponse.token_type, loginResponse.access_token)
            ])

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map result = getJsonResponse(response)
            System.out.println(result)
            assert result.username == username
            assert result.name == name
            assert result.student_id == student_id

        where:
            username   | password      | name        | student_id
            "username" | "supersecret" | "some-name" | "some-student-id"
    }

    /*****************************************************************************************
     ********** EDIT USER ********************************************************************
     *****************************************************************************************/

    @Unroll
    void "test edit current profile should update user data"() {
        when:
            def response = patch("/api/auth/profile", updatedFields, username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map result = getJsonResponse(response)

            ["name", "student_id", "surname", "username", "email", "university", "degree"].each { key ->
                assert result[key] == (updatedFields[key] ? updatedFields[key] : user.getAt(underscoreToCamelCase(key)))
            }

        where:
            updatedFields << [
                    [:],
                    [ name: "other-name" ],
                    [ university: "other-university"],
                    [ name: "other-name", university: "other-university" ]
            ]
    }

    @Unroll
    void "test edit unmutable properties from profile should not update user data"() {
        when:
            def response = patch("/api/auth/profile", updatedFields, username, password)

        then:
            response.contentType == "application/json"
            response.statusCode == SC_OK

            Map result = getJsonResponse(response)

            updatedFields.each { key, _ ->
                assert result[key] == user.getAt(key)
            }

        where:
            updatedFields << [
                    [ username: "other-username"],
                    [ id: 22 ],
            ]
    }
}


