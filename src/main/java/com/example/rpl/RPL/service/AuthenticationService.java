package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthenticationService {

    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public User createUser(String name, String surname, String studentId, String username,
        String email, String password, String university, String degree) {
        User user = new User(name, surname, studentId, username, email,
            new BCryptPasswordEncoder().encode(password), university, degree);

        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(String.format("Email '%s' already used", email),
                "ERROR_EMAIL_USED");
        }

        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(
                String.format("Username '%s' already used", username), "ERROR_USERNAME_USED");
        }

        return userRepository.save(user);
    }
}
