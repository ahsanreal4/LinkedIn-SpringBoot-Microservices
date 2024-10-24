package com.linkedIn.company_job_service.dto.company.locations;

import com.linkedIn.company_job_service.validator.atLeastOneNotNull.AtLeastOneNotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@AtLeastOneNotNull
@Data
public class UpdateCompanyLocationDto {
    @Size(min = 5, max = 255)
    private String address;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 40)
    private String city;

    @Size(max = 40)
    private String country;
}
