package com.linkedIn.users_service.dto.experience;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDto {
    private long id;
    private long userId;
    private String company;
    private String position;
    private String description;
    private float experienceInYears;
    private LocalDate startDate;
    private LocalDate endDate;
}
