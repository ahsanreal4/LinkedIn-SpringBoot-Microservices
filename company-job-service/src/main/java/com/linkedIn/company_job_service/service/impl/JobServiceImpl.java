package com.linkedIn.company_job_service.service.impl;

import com.linkedIn.company_job_service.dto.company.CompanyDto;
import com.linkedIn.company_job_service.dto.job.*;
import com.linkedIn.company_job_service.entity.Category;
import com.linkedIn.company_job_service.entity.company.Company;
import com.linkedIn.company_job_service.entity.company.CompanyFiles;
import com.linkedIn.company_job_service.entity.job.Job;
import com.linkedIn.company_job_service.entity.user.User;
import com.linkedIn.company_job_service.enums.CompanyFileType;
import com.linkedIn.company_job_service.enums.JobType;
import com.linkedIn.company_job_service.exception.ApiException;
import com.linkedIn.company_job_service.feign_clients.impl.UserServiceClientImpl;
import com.linkedIn.company_job_service.repository.CategoryRepository;
import com.linkedIn.company_job_service.repository.company.CompanyRepository;
import com.linkedIn.company_job_service.repository.job.JobRepository;
import com.linkedIn.company_job_service.service.JobService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final UserServiceClientImpl userService;

    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,
                          CategoryRepository categoryRepository, UserServiceClientImpl userService) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public void createJob(CreateJobDto createJobDto, long userId) {
        JobType jobType = returnJobTypeOrThrowError(createJobDto.getType());

        Company company = returnCompanyOrThrowError(createJobDto.getCompanyId());
        Category category = returnCategoryOrThrowError(createJobDto.getCategoryId());

        User user = new User();
        user.setId(userId);

        Job job = new Job();
        job.setCountry(createJobDto.getCountry());
        job.setCity(createJobDto.getCity());
        job.setCompany(company);
        job.setPosition(createJobDto.getPosition());
        job.setDescription(createJobDto.getDescription());
        job.setType(jobType);
        job.setPostedAt(new Date());
        job.setPostedBy(user);
        job.setCategory(category);

        jobRepository.save(job);
    }

    @Override
    public void updateJob(UpdateJobDto updateJobDto, long jobId) {
        Job job = returnJobOrThrowError(jobId);

        if(updateJobDto.getType() != null) {
            JobType jobType = returnJobTypeOrThrowError(updateJobDto.getType());
            job.setType(jobType);
        }
        if(updateJobDto.getCategoryId() != null) {
            Category category = returnCategoryOrThrowError(updateJobDto.getCategoryId());
            job.setCategory(category);
        }
        if(updateJobDto.getPosition() != null) job.setPosition(updateJobDto.getPosition());
        if(updateJobDto.getDescription() != null) job.setDescription(updateJobDto.getDescription());
        if(updateJobDto.getCountry() != null) job.setDescription(updateJobDto.getDescription());
        if(updateJobDto.getCity() != null) job.setCity(updateJobDto.getCity());

        jobRepository.save(job);
    }

    @Override
    public JobDto getJobById(long jobId) {
        Job job = returnJobOrThrowError(jobId);

        return mapToJobDto(job);
    }

    @Override
    public DetailedJobDto getDetailedJobById(long jobId) {
        Job job = returnJobOrThrowError(jobId);

        return mapToDetailedJobDto(job);
    }

    @Override
    public List<JobDto> getAllJobs() {
        List<Job> jobs = this.jobRepository.findAll();

        return jobs.stream().map(this::mapToJobDto).collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getJobsByCategory(long categoryId) {
        Category category = returnCategoryOrThrowError(categoryId);
        List<Job> jobs = jobRepository.findByCategoryId(category.getId());

        return jobs.stream().map(this::mapToJobDto).collect(Collectors.toList());
    }

    @Override
    public void deleteJobById(long jobId, long userId, boolean isAdmin) {
        Job job = returnJobOrThrowError(jobId);

        if(!job.getPostedBy().getId().equals(userId) && !isAdmin) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot delete someone else post");

        jobRepository.delete(job);
    }

    private DetailedJobDto mapToDetailedJobDto(Job job) {
        DetailedJobDto dto = new DetailedJobDto();

        Company company = job.getCompany();
        Category category = job.getCategory();

        dto.setId(job.getId());
        dto.setDescription(job.getDescription());
        dto.setCountry(job.getCountry());
        dto.setCity(job.getCity());
        dto.setCategory(category.getName());
        dto.setCompany(getCompanyDto(company));
        dto.setType(job.getType().toString());
        dto.setPosition(job.getPosition());
        dto.setPostedAt(job.getPostedAt());
        dto.setPostedBy(getJobUserDto(job.getPostedBy().getId()));

        return dto;
    }

    private UserProfileDto getJobUserDto(long userId) {
        try {
            return this.userService.getUserProfile(userId);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Some error occurred");
        }
    }

    private CompanyDto getCompanyDto(Company company) {
        CompanyDto dto = new CompanyDto();
        Set<CompanyFiles> companyFilesSet = company.getFiles();

        if(companyFilesSet != null && !companyFilesSet.isEmpty()) {
            for(CompanyFiles file: companyFilesSet) {
                if (file.getType().equals(CompanyFileType.LOGO)) dto.setLogo(file.getLink());
            }
        }

        dto.setName(company.getName());
        dto.setWebsite(company.getWebsite());
        dto.setNumEmployees(company.getNumEmployees());
        dto.setHeadLine(company.getHeadLine());

        return dto;
    }

    private JobDto mapToJobDto(Job job) {
        Company company = job.getCompany();
        Category category = job.getCategory();

        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setDescription(job.getDescription());
        dto.setCountry(job.getCountry());
        dto.setCity(job.getCity());
        dto.setCategory(category.getName());
        dto.setCompany(getCompanyDto(company));
        dto.setType(job.getType().toString());
        dto.setPosition(job.getPosition());
        dto.setPostedAt(job.getPostedAt());

        return dto;
    }

    private Job returnJobOrThrowError(long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job with id " + jobId + " was not found"));
    }

    private JobType returnJobTypeOrThrowError(String type) {
        JobType jobType;

        try {
            jobType = JobType.valueOf(type.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Job Type. Only " + JobType.ONSITE + ", " + JobType.HYBRID + ", " + JobType.REMOTE + " are supported");
        }

        return jobType;
    }

    private Company returnCompanyOrThrowError(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Company with id " + companyId + " was not found"));
    }

    private Category returnCategoryOrThrowError(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " was not found"));
    }

}
