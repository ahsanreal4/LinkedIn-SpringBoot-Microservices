package com.linkedIn.users_service.dto.experience;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateExperienceDto {
    @NotEmpty
    @Size(min = 10)
    private String description;

    @NotEmpty
    @Size(min = 3, max = 255)
    private String company;

    @NotEmpty
    @Size(min = 8, max = 255)
    private String position;

    @NotNull
    @Positive
    private Float experienceInYears;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
