package com.linkedIn.company_job_service.dto.job.applied_job;

import com.linkedIn.company_job_service.dto.job.UserProfileDto;
import lombok.Data;

import java.util.Date;

@Data
public class AppliedJobDto {
    private long id;
    private UserProfileDto appliedBy;
    private ResumeDto resume;
    private Date appliedAt;
}
