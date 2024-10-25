package com.linkedIn.company_job_service.dto.job;

import com.linkedIn.company_job_service.dto.company.CompanyDto;
import lombok.Data;

import java.util.Date;

@Data
public class JobDto {
    private long id;
    private CompanyDto company;
    private String category;
    private String description;
    private String position;
    private String type;
    private String city;
    private String country;
    private Date postedAt;
}