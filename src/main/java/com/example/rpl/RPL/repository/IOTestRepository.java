package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.IOTest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOTestRepository extends JpaRepository<IOTest, Long> {

    List<IOTest> findAllByActivity_Id(Long activityId);
}
