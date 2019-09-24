package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.CourseUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findByIdAndUser_Id(Long courseId, Long userId);
}