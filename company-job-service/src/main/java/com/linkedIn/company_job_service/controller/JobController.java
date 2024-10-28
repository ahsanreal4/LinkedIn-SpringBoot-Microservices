package com.linkedIn.company_job_service.controller;

import com.linkedIn.company_job_service.dto.job.CreateJobDto;
import com.linkedIn.company_job_service.dto.job.DetailedJobDto;
import com.linkedIn.company_job_service.dto.job.JobDto;
import com.linkedIn.company_job_service.dto.job.UpdateJobDto;
import com.linkedIn.company_job_service.service.JobService;
import com.linkedIn.company_job_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final UserUtils userUtils;

    public JobController(JobService jobService, UserUtils userUtils) {
        this.jobService = jobService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public void createJob(@Valid @RequestBody CreateJobDto createJobDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        this.jobService.createJob(createJobDto, userId);
    }

    @PutMapping("/{id}")
    public void updateJob(@Valid @RequestBody UpdateJobDto updateJobDto, @PathVariable("id") long id, HttpServletRequest request) {
        this.jobService.updateJob(updateJobDto, id);
    }

    @GetMapping("")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        return ResponseEntity.ok(this.jobService.getAllJobs());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<JobDto>> getJobsByCategory(@PathVariable("id") long categoryId) {
        return ResponseEntity.ok(this.jobService.getJobsByCategory(categoryId));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<List<JobDto>> getJobsByCompany(@PathVariable("id") long companyId) {
        return ResponseEntity.ok(this.jobService.getJobsByCompanyId(companyId));
    }

    @GetMapping("/sorted/{sortType}")
    public ResponseEntity<List<JobDto>> getJobsBySortedDate(@PathVariable("sortType") String sortType) {
        return ResponseEntity.ok(this.jobService.getJobsSortedByDate(sortType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.jobService.getJobById(id));
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<DetailedJobDto> getDetailedJobById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.jobService.getDetailedJobById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteJobById(@PathVariable("id") long id, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);
        boolean isAdmin = userUtils.isAdmin(request);

        this.jobService.deleteJobById(id, userId, isAdmin);
    }
}
