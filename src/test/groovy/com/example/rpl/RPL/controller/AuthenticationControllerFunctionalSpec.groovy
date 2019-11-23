package com.example.rpl.RPL.controller

import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import static javax.servlet.http.HttpServletResponse.SC_CREATED

@ActiveProfiles("test-functional")
class AuthenticationControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    UserRepository userRepository

    def "Create user with correct values must save user in DB"() {
        given: "a new user"
            def body = [
                    username  : "alep1234",
                    email     : "asd@asd.com",
                    password  : "12345",
                    name      : "Ale",
                    surname   : "Levinas",
                    studentId : "95719",
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
            assert user.student_id == body.studentId
            assert user.degree == body.degree
            assert user.university == body.university

            assert user.email_validated == false
            assert user.date_created != null
            assert user.last_updated == user.date_created

            assert userRepository.existsById(user.id as Long)
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
                    studentId : "95719",
                    degree    : "Ing. Informatica",
                    university: "UBA"
            ]

        when: "post new user"
            def response = post("/api/auth/signup", body)

        then: "must return a new saved User"
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
    void "should fail hubernate validation #test creating user"() {
        given: "a new user"
            def body = [
                    username  : username,
                    email     : email,
                    password  : password,
                    name      : "Ale",
                    surname   : "Levinas",
                    studentId : "95719",
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
}


