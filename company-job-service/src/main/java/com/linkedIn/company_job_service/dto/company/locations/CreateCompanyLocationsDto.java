package com.linkedIn.company_job_service.dto.company.locations;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCompanyLocationsDto {
    @NotNull
    private Long companyId;

    @NotEmpty
    @Valid
    private CreateCompanyLocationDto[] locations;
}
