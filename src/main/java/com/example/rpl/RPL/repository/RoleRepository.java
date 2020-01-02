package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Role;
import com.example.rpl.RPL.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}