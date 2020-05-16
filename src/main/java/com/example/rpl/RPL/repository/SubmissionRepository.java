package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<ActivitySubmission, Long>, JpaSpecificationExecutor<ActivitySubmission> {

    List<ActivitySubmission> findAllByUserAndActivityIn(User user, List<Activity> activities);

    List<ActivitySubmission> findAllByUserAndActivity_Id(User user, Long activityId);
}