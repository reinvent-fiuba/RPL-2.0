package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.repository.ValidationTokenRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class UsersServiceSpec extends Specification {

    private UsersService usersService
    private UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        usersService = new UsersService(userRepository)
    }

    void "should find users successfully"() {
        when:
            List<User> users = usersService.findUsers(query)

        then:
            1 * userRepository.findByQueryString(searchQuery) >> result

            users.size() == result.size()

        where:
            query   | searchQuery | result
            null    | ""          | []
            ""      | ""          | []
            "query" | "query"     | [Mock(User)]
    }
}
