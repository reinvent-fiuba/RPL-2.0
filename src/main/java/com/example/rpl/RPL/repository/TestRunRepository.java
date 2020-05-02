package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {

    TestRun findByActivitySubmission_Id(Long activitySubmissionId);

    TestRun findTopByActivitySubmission_IdOrderByLastUpdatedDesc(Long activitySubmissionId);
}