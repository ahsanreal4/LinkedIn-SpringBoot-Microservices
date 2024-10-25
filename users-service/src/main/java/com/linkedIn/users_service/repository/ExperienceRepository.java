package com.linkedIn.users_service.repository;

import com.linkedIn.users_service.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
