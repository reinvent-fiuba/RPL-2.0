package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Activity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends
        JpaRepository<Activity, Long>,
        JpaSpecificationExecutor<Activity> {

    List<Activity> findActivitiesByCourse_Id(Long courseId);

    List<Activity> findActivitiesByCourse_IdAndActiveAndDeleted(Long courseId, Boolean active, Boolean deleted);
}
