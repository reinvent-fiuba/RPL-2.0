package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.exception.NotFoundException
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.RoleRepository
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.repository.ValidationTokenRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class AuthenticationServiceSpec extends Specification {

    private AuthenticationService authenticationService
    private UserRepository userRepository
    private PasswordEncoder passwordEncoder
    private ValidationTokenRepository validationTokenRepository
    private EmailService emailService
    private RoleRepository roleRepository

    def setup() {
        userRepository = Mock(UserRepository)
        passwordEncoder = Mock(PasswordEncoder)
        validationTokenRepository = Mock(ValidationTokenRepository)
        emailService = Mock(EmailService)
        roleRepository = Mock(RoleRepository)
        authenticationService = new AuthenticationService(userRepository, validationTokenRepository, emailService, passwordEncoder, roleRepository)
    }

    void "should create user successfully"() {
        given:
            String password = "1234"
            String email = "asd@gmail.com"
            String username = "alepox"

        when:
            User newUser = authenticationService.createUser("Ale", "Levinas", "95719", username, email, password, "UBA", "Ing. Informatica")

        then:
            1 * passwordEncoder.encode(password) >> password
            1 * userRepository.existsByEmail(email) >> false
            1 * userRepository.existsByUsername(username) >> false

            1 * userRepository.save(_ as User) >> { User user -> return user }

            assert newUser.username == username
            assert newUser.email == email
            assert newUser.password == password
    }

    @Unroll
    void "should fail to create user if #property exists"() {
        given:
            String password = "1234"
            String email = "asd@gmail.com"
            String username = "alepox"

        when:
            User newUser = authenticationService.createUser("Ale", "Levinas", "95719", username, email, password, "UBA", "Ing. Informatica")

        then:
            1 * passwordEncoder.encode(password) >> password
            userRepository.existsByEmail(email) >> emailExists
            userRepository.existsByUsername(username) >> usernameExists


            thrown(EntityAlreadyExistsException)

        where:
            property   | usernameExists | emailExists
            "username" | true           | false
            "email"    | false          | true
    }

    @Unroll
    void "should update user successfully"() {
        given:
            User user = Mock(User)

        when:
            authenticationService.updateUser(userId, name, surname, studentId, email, university, degree)

        then:
            1 * userRepository.findById(userId) >> Optional.of(user)
            if (name != null) 1*user.setName(name)
            if (surname != null) 1*user.setSurname(surname)
            if (studentId != null) 1*user.setStudentId(studentId)
            if (email != null) 1*user.setEmail(email)
            if (university != null) 1*user.setUniversity(university)
            if (degree != null) 1*user.setDegree(name)
            1 * userRepository.save(user)

        where:
            userId | name   | surname | studentId | email | university | degree
            1      | "ale"  | null    | null      | null  | null       | null
            2      | null   | "cano"  | "97925"   | null  | null       | null
            3      | null   | null    | null      | null  | null       | null
    }

    @Unroll
    void "should throw error if user doesnt exist"() {
        when:
            authenticationService.updateUser(1, "name", "surname", "studentId", "email", "university", "degree")

        then:
            1 * userRepository.findById(1) >> Optional.empty()

            thrown(NotFoundException)
    }
}
