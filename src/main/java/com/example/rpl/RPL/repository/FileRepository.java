package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.RPLFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<RPLFile, Long> {

}