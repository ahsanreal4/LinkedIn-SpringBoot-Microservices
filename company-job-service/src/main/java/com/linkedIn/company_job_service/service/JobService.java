package com.linkedIn.company_job_service.service;

import com.linkedIn.company_job_service.dto.job.CreateJobDto;
import com.linkedIn.company_job_service.dto.job.DetailedJobDto;
import com.linkedIn.company_job_service.dto.job.JobDto;
import com.linkedIn.company_job_service.dto.job.UpdateJobDto;

import java.util.List;

public interface JobService {
    void createJob(CreateJobDto createJobDto, long userId);
    void updateJob(UpdateJobDto updateJobDto, long jobId);
    JobDto getJobById(long jobId);
    DetailedJobDto getDetailedJobById(long jobId);
    List<JobDto> getAllJobs();
    List<JobDto> getJobsByCategory(long categoryId);
    void deleteJobById(long jobId, long userId, boolean isAdmin);
}
