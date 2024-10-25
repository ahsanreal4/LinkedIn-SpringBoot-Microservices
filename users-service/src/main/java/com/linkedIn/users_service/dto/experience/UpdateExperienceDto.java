package com.linkedIn.users_service.dto.experience;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkedIn.users_service.validators.atLeastOneNotNull.AtLeastOneNotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@AtLeastOneNotNull
@Data
public class UpdateExperienceDto {
    @Size(min = 10)
    private String description;

    @Size(min = 3, max = 255)
    private String company;

    @Size(min = 8, max = 255)
    private String position;

    @Positive
    private Float experienceInYears;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
