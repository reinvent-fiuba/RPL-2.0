package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {

}