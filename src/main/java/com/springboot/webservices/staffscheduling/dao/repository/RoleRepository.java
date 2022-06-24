package com.springboot.webservices.staffscheduling.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.webservices.staffscheduling.dao.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
