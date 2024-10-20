package com.linkedin.auth_service.repository;

import com.linkedin.auth_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Boolean existsByName(String name);
}
