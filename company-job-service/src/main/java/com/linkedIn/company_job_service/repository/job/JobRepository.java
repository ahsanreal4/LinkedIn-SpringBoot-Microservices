package com.linkedIn.company_job_service.repository.job;

import com.linkedIn.company_job_service.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCategoryId(long categoryId);
    List<Job> findByCompanyId(long companyId);
}
