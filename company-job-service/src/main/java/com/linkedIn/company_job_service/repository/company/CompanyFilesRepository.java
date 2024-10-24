package com.linkedIn.company_job_service.repository.company;

import com.linkedIn.company_job_service.entity.company.CompanyFiles;
import com.linkedIn.company_job_service.enums.CompanyFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyFilesRepository extends JpaRepository<CompanyFiles, Long> {
}
