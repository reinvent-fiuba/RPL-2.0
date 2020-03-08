package com.example.rpl.RPL.security;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.CourseUserRepository;
import com.example.rpl.RPL.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private CourseUserRepository courseUserRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
        CourseUserRepository courseUserRepository) {
        this.userRepository = userRepository;
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
        throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "User not found with username or email : " + usernameOrEmail)
            );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(String.format("User with ID %s not found", userId),
                "user_not_found")
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByIdAndCourseId(Long userId, Long courseId) {
        if (courseId != null) {
            Optional<CourseUser> courseUser = courseUserRepository
                .findByCourse_IdAndUser_Id(courseId, userId);
            if (courseUser.isPresent()) {
                log.info("Getting UserDetails for UserID:{} and CourseID:{}", userId, courseId);
                return UserPrincipal.create(courseUser.get());
            }
        }

        log.info("Getting UserDetails for UserID:{}", userId);
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(String.format("User with ID %s not found", userId),
                "user_not_found")
        );

        return UserPrincipal.create(user);
    }
}