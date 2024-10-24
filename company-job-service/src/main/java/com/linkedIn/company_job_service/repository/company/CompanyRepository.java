package com.linkedIn.company_job_service.repository.company;

import com.linkedIn.company_job_service.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
