package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.ActivitySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<ActivitySubmission, Long> {

}