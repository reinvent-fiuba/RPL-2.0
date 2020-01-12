package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {
    List<ActivityCategory> findByCourse_Id(Long courseId);
}