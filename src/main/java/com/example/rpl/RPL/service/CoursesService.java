package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.model.CourseSemester;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.CourseSemesterRepository;
import com.example.rpl.RPL.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CoursesService {

    private CourseSemesterRepository courseSemesterRepository;

    @Autowired
    public CoursesService(CourseSemesterRepository courseSemesterRepository) {
        this.courseSemesterRepository = courseSemesterRepository;
    }

    /**
     * Get all Courses.
     * @return a new saved User
     */
    @Transactional
    public List<CourseSemester> getAllCourseSemester() {
        return courseSemesterRepository.findAll();
//        User user = new User(name, surname, studentId, username, email,
//            passwordEncoder.encode(password), university, degree);

//        if (userRepository.existsByEmail(email)) {
//            throw new EntityAlreadyExistsException(String.format("Email '%s' already used", email),
//                "ERROR_EMAIL_USED");
//        }
//
//        if (userRepository.existsByUsername(username)) {
//            throw new EntityAlreadyExistsException(
//                String.format("Username '%s' already used", username), "ERROR_USERNAME_USED");
//        }

//        log.info("[process:create_user][username:{}] Creating new user", username);

//        return userRepository.save(user);
    }
}
