package com.linkedIn.company_job_service.service;

import com.linkedIn.company_job_service.dto.job.applied_job.AppliedJobDto;
import com.linkedIn.company_job_service.dto.job.applied_job.CreateAppliedJobDto;

import java.util.List;

public interface AppliedJobService {
    void createAppliedJob(CreateAppliedJobDto createAppliedJobDto, long userId);
    List<AppliedJobDto> getAppliedJobsByJobId(long jobId);
}
