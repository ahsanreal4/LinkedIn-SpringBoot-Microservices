package com.linkedIn.company_job_service.controller;

import com.linkedIn.company_job_service.dto.job.applied_job.AppliedJobDto;
import com.linkedIn.company_job_service.dto.job.applied_job.CreateAppliedJobDto;
import com.linkedIn.company_job_service.service.AppliedJobService;
import com.linkedIn.company_job_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/applied")
public class AppliedJobController {
    private final UserUtils userUtils;
    private final AppliedJobService appliedJobService;

    public AppliedJobController(UserUtils userUtils, AppliedJobService appliedJobService){
        this.appliedJobService = appliedJobService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public void createAppliedJob(@Valid @RequestBody CreateAppliedJobDto createAppliedJobDto, HttpServletRequest request){
        long userId = this.userUtils.getUserId(request);

        this.appliedJobService.createAppliedJob(createAppliedJobDto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AppliedJobDto>> getAppliedJobsByJobId(@PathVariable("id") long id) {
        List<AppliedJobDto> jobs = this.appliedJobService.getAppliedJobsByJobId(id);

        return ResponseEntity.ok(jobs);
    }
}
