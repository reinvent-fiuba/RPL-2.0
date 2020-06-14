package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.model.ValidationToken;
import com.example.rpl.RPL.repository.UserRepository;
import com.example.rpl.RPL.repository.ValidationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UsersService {

    private final UserRepository userRepository;

    @Autowired
    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<User> findUsers(String queryString) {
        List<User> users = userRepository.findByQueryString(queryString != null ? queryString : "");
        return users;
    }
}
