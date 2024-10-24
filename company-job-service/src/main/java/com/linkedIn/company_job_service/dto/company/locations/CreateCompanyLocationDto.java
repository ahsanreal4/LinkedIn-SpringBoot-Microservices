package com.linkedIn.company_job_service.dto.company.locations;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCompanyLocationDto {
    @Size(min = 5, max = 255)
    private String address;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 40)
    @NotEmpty
    private String city;

    @Size(max = 40)
    @NotEmpty
    private String country;
}
