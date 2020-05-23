package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.ValidationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {

    Optional<ValidationToken> findByToken(String token);
}