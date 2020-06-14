package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("select u " +
            "from User u " +
            "where u.username like ?1% OR u.name like ?1% OR u.surname like ?1% "
    )
    List<User> findByQueryString(String query);
}