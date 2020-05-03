package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.IOTestRun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOTestRunRepository extends JpaRepository<IOTestRun, Long> {

    List<IOTestRun> findAllByTestRun_Id(Long testRunId);
}