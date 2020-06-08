package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.ValidationToken;
import com.example.rpl.RPL.repository.UserRepository;
import com.example.rpl.RPL.repository.ValidationTokenRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ValidationTokenRepository validationTokenRepository;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
        ValidationTokenRepository ValidationTokenRepository,
        EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.validationTokenRepository = ValidationTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new User.
     *
     * @return a new saved User
     * @throws EntityAlreadyExistsException if email or username exists ValidationException declared
     * on the User class
     */
    @Transactional
    public User createUser(String name, String surname, String studentId, String username,
        String email, String password, String university, String degree) {
        User user = new User(name, surname, studentId, username, email,
            passwordEncoder.encode(password), university, degree);

        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(String.format("Email '%s' already used", email),
                "ERROR_EMAIL_USED");
        }

        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(
                String.format("Username '%s' already used", username), "ERROR_USERNAME_USED");
        }

        log.info("[process:create_user][username:{}] Creating new user", username);
        return userRepository.save(user);
    }

    @Transactional
    public List<User> findUsers(String queryString) {
        List<User> users = userRepository.findByQueryString(queryString);
        return users;
    }

    @Transactional
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id '%d' does not exist", userId));
        }

        return user.get();
    }

    @Transactional
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() ->
                new NotFoundException(String.format("User '%s' does not exist", usernameOrEmail)));
    }

    @Transactional
    public ValidationToken validateToken(String requestToken) {
        ValidationToken token = validationTokenRepository.findByToken(requestToken)
            .orElseThrow(
                () -> new NotFoundException("El token no existe o expiró", "token_not_found"));

        if (token.getExpiryDate().isBefore(ZonedDateTime.now())) {
            throw new NotFoundException("El token no existe o expiró", "token_not_found");
        }
        return token;
    }

    /*
     ********* RESET PASSWORD *******
     */

    @Transactional
    public void sendResetPasswordToken(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new NotFoundException(String.format("User with email '%s' does not exist", email),
                "user_not_found"));

        String token = UUID.randomUUID().toString();

        ValidationToken myToken = new ValidationToken(token, user);
        validationTokenRepository.save(myToken);

        emailService.sendResetPasswordMessage(email, token);
    }

    @Transactional
    public User resetPassword(ValidationToken token, @NotNull String newPassword) {
        User user = token.getUser();
        user.changePassword(passwordEncoder.encode(newPassword));

        return userRepository.save(user);
    }

    /*
     ********* VALIDATE EMAIL *******
     */

    @Transactional
    public void sendValidateEmailToken(User user) {
        String token = UUID.randomUUID().toString();

        ValidationToken myToken = new ValidationToken(token, user);
        validationTokenRepository.save(myToken);

        emailService.sendValidateEmailMessage(user.getEmail(), token);
    }

    @Transactional
    public User validateEmail(User user) {
        user.markAsValidated();
        return userRepository.save(user);
    }
}
