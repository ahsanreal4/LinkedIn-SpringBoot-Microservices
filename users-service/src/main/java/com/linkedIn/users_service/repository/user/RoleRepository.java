package com.linkedIn.users_service.repository.user;

import com.linkedIn.users_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Boolean existsByName(String name);
}
