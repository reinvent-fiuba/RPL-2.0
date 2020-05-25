package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findActivitiesByCourse(Course course);

}