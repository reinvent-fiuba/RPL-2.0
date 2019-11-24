package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.CourseSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSemesterRepository extends JpaRepository<CourseSemester, Long> {

}