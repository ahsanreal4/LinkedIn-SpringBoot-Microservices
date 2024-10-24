package com.linkedIn.company_job_service.service.impl;

import com.linkedIn.company_job_service.dto.job.UserProfileDto;
import com.linkedIn.company_job_service.dto.job.applied_job.AppliedJobDto;
import com.linkedIn.company_job_service.dto.job.applied_job.CreateAppliedJobDto;
import com.linkedIn.company_job_service.dto.job.applied_job.ResumeDto;
import com.linkedIn.company_job_service.entity.job.AppliedJobs;
import com.linkedIn.company_job_service.entity.job.Job;
import com.linkedIn.company_job_service.entity.user.User;
import com.linkedIn.company_job_service.entity.user.UserFiles;
import com.linkedIn.company_job_service.enums.UserFileType;
import com.linkedIn.company_job_service.exception.ApiException;
import com.linkedIn.company_job_service.feign_clients.impl.UserServiceClientImpl;
import com.linkedIn.company_job_service.repository.job.AppliedJobsRepository;
import com.linkedIn.company_job_service.repository.job.JobRepository;
import com.linkedIn.company_job_service.repository.user.UserFileRepository;
import com.linkedIn.company_job_service.service.AppliedJobService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppliedJobServiceImpl implements AppliedJobService {

    private final AppliedJobsRepository appliedJobsRepository;
    private final JobRepository jobRepository;
    private final UserFileRepository userFileRepository;
    private final UserServiceClientImpl userServiceClient;

    public AppliedJobServiceImpl(AppliedJobsRepository appliedJobsRepository, JobRepository jobRepository,
                                 UserServiceClientImpl userServiceClient, UserFileRepository userFileRepository) {
        this.appliedJobsRepository = appliedJobsRepository;
        this.jobRepository = jobRepository;
        this.userServiceClient = userServiceClient;
        this.userFileRepository = userFileRepository;
    }

    @Override
    public void createAppliedJob(CreateAppliedJobDto createAppliedJobDto, long userId) {
        Job job = returnJobOrThrowError(createAppliedJobDto.getJobId());

        User user = new User();
        user.setId(userId);

        UserFiles resume = returnResumeOrThrowError(createAppliedJobDto.getResumeId());

        AppliedJobs appliedJob = new AppliedJobs();
        appliedJob.setJob(job);
        appliedJob.setAppliedBy(user);
        appliedJob.setResume(resume);
        appliedJob.setAppliedAt(new Date());

        try {
            appliedJobsRepository.save(appliedJob);
        }
        catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.NOT_FOUND, "User resume with id " + createAppliedJobDto.getResumeId() + " was not found");
        }
    }

    @Override
    public List<AppliedJobDto> getAppliedJobsByJobId(long jobId) {
        List<AppliedJobs> jobs = this.appliedJobsRepository.findByJobId(jobId);

        return jobs.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    private AppliedJobDto mapToDto(AppliedJobs job) {
        AppliedJobDto dto = new AppliedJobDto();
        dto.setId(job.getId());
        dto.setAppliedAt(job.getAppliedAt());

        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setLink(job.getResume().getLink());
        resumeDto.setName(job.getResume().getName());

        dto.setResume(resumeDto);

        UserProfileDto userProfileDto = getUserProfile(job.getAppliedBy().getId());
        if(userProfileDto != null) dto.setAppliedBy(userProfileDto);

        return dto;
    }

    private UserProfileDto getUserProfile(long userId){
        try {
            return this.userServiceClient.getUserProfile(userId);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private Job returnJobOrThrowError(long jobId) {
        return this.jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "job with id " + jobId + " was not found"));
    }

    private UserFiles returnResumeOrThrowError(long fileId) {
        Optional<UserFiles> optionalFile = this.userFileRepository.findById(fileId);

        if (optionalFile.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "User resume with id " + fileId + " was not found");

        UserFiles file = optionalFile.get();

        if(!file.getType().equals(UserFileType.RESUME)) throw new ApiException(HttpStatus.NOT_FOUND, "User file found with id " + fileId + " is not a resume");

        return file;
    }
}
