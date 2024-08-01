package com.example.rpl.RPL.controller

import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll

import static jakarta.servlet.http.HttpServletResponse.*

@ActiveProfiles("test-functional")
class UsersControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    String username

    String password

    def setup() {
        User user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                passwordEncoder.encode('supersecret'),
                'some-university',
                'some-hard-degree',

        )

        username = user.getUsername()
        password = 'supersecret'

        user.setIsAdmin(true)
        user.markAsValidated()
        userRepository.save(user)
    }

    def cleanup() {
        userRepository.deleteAll()
    }

    /*****************************************************************************************
     ********** FIND USERS *******************************************************************
     *****************************************************************************************/

    def "Find users without query string should retrieve all users"() {
        given: "multiple users"
            def numberOfUsers = 5;
            for (def i=0; i<numberOfUsers; i++) {
                userRepository.save(new User(
                        'some-name' + i.toString(),
                        'some-surname' + i.toString(),
                        'some-student-id',
                        'username' + i.toString(),
                        'some@mail.com' + i.toString(),
                        passwordEncoder.encode('supersecret'),
                        'some-university',
                        'some-hard-degree'
                ))
            }

        when: "find all users"
            def response = get("/api/users", username, password)

        then: "must return a new saved User"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            List users = getJsonResponse(response)

            users.size() == 6
    }

    def "Find users with empty query string should retrieve all users"() {
        given: "multiple users"
            def numberOfUsers = 5;
            for (def i=0; i<numberOfUsers; i++) {
                userRepository.save(new User(
                        'some-name' + i.toString(),
                        'some-surname' + i.toString(),
                        'some-student-id',
                        'username' + i.toString(),
                        'some@mail.com' + i.toString(),
                        passwordEncoder.encode('supersecret'),
                        'some-university',
                        'some-hard-degree'
                ))
            }

        when: "find all users"
            def response = get("/api/users?query=", username, password)

        then: "must return al users"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            List users = getJsonResponse(response)

            users.size() == 6
    }

    def "Find users with query string should retrieve all users"() {
        given: "multiple users"
            def numberOfUsers = 5;
            for (def i=0; i<numberOfUsers; i++) {
                userRepository.save(new User(
                        'some-name' + i.toString(),
                        'some-surname' + i.toString(),
                        'some-student-id',
                        'username' + i.toString(),
                        'some@mail.com' + i.toString(),
                        passwordEncoder.encode('supersecret'),
                        'some-university',
                        'some-hard-degree'
                ))
            }

        when: "find all users"
            def response = get("/api/users?query=${queryString}", username, password)

        then: "must return al users"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            List users = getJsonResponse(response)

            users.size() == 6

        where:
            queryString << ['some-name', 'some-surname', 'username']
    }

    def "Find users with specific query string should retrieve all users"() {
        given: "multiple users"
            def numberOfUsers = 5;
            for (def i=0; i<numberOfUsers; i++) {
                userRepository.save(new User(
                        'some-name' + i.toString(),
                        'some-surname' + i.toString(),
                        'some-student-id',
                        'username' + i.toString(),
                        'some@mail.com' + i.toString(),
                        passwordEncoder.encode('supersecret'),
                        'some-university',
                        'some-hard-degree'
                ))
            }

        when: "find all users"
            def response = get("/api/users?query=${queryString}", username, password)

        then: "must return al users"
            response.contentType == "application/json"
            response.statusCode == SC_OK

            List users = getJsonResponse(response)

            users.size() == 1

        where:
            queryString << ['some-name0', 'some-surname0', 'username0']
    }

}
