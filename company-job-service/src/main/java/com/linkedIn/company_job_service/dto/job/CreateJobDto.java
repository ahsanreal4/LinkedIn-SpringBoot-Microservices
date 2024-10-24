package com.linkedIn.company_job_service.dto.job;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateJobDto {
    @NotNull
    private Long companyId;

    @NotNull
    private Long categoryId;

    @NotEmpty
    @Size(min = 50)
    private String description;

    @NotEmpty
    @Size(min = 5, max = 100)
    private String position;

    @NotEmpty
    @Size(min = 5, max = 10)
    private String type;

    @NotEmpty
    @Size(min = 3, max = 255)
    private String city;

    @NotEmpty
    @Size(min = 3, max = 255)
    private String country;
}
