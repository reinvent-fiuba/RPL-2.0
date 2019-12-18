package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.UnitTest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitTestRepository extends JpaRepository<UnitTest, Long> {
    Optional<UnitTest> findByActivity_Id(Long activityId);
}
