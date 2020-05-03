package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.UnitTestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitTestRunRepository extends JpaRepository<UnitTestRun, Long> {

}