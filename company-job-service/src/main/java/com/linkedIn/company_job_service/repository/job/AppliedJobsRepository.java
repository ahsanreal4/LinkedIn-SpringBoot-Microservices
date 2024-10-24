package com.linkedIn.company_job_service.repository.job;

import com.linkedIn.company_job_service.entity.job.AppliedJobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppliedJobsRepository extends JpaRepository<AppliedJobs, Long> {
    List<AppliedJobs> findByJobId(long jobId);
}
