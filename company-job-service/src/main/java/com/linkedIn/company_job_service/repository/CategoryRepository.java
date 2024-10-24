package com.linkedIn.company_job_service.repository;

import com.linkedIn.company_job_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
