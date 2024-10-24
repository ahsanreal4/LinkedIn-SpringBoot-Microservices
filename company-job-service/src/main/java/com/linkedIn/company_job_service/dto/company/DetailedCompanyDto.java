package com.linkedIn.company_job_service.dto.company;

import com.linkedIn.company_job_service.dto.company.locations.CompanyLocationDto;
import lombok.Data;

@Data
public class DetailedCompanyDto {
    private long id;
    private String name;
    private String numEmployees;
    private long createdBy;
    private String about;
    private String headLine;
    private String website;
    private String logo;
    private String banner;
    private CompanyLocationDto[] locations;
}
