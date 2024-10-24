package com.linkedIn.company_job_service.dto.company;

import lombok.Data;

@Data
public class CompanyDto {
    private long id;
    private long createdBy;
    private String name;
    private String numEmployees;
    private String about;
    private String headLine;
    private String website;
    private String logo;
}
