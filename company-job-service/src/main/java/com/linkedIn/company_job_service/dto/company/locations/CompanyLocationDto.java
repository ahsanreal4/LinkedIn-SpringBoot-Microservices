package com.linkedIn.company_job_service.dto.company.locations;


import lombok.Data;

@Data
public class CompanyLocationDto {
    private long id;
    private String address;
    private String zipCode;
    private String city;
    private String country;
}
