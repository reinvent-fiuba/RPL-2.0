package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
