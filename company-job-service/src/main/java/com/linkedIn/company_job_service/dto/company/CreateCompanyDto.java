package com.linkedIn.company_job_service.dto.company;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCompanyDto {
    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    @Size(min = 8, max = 255)
    private String about;

    @NotEmpty
    @Size(min = 4, max = 10)
    private String numEmployees;

    @Size(min = 10, max = 255)
    private String website;

    @Size(min = 5, max = 100)
    private String headLine;
}
