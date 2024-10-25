package com.linkedIn.users_service.service;

import com.linkedIn.users_service.dto.experience.CreateExperienceDto;
import com.linkedIn.users_service.dto.experience.ExperienceDto;
import com.linkedIn.users_service.dto.experience.UpdateExperienceDto;

import java.util.List;

public interface ExperienceService {
    void createExperience(CreateExperienceDto createExperienceDto, long userId);
    void updateExperienceDto(UpdateExperienceDto updateExperienceDto, long experienceId, long userId);
    List<ExperienceDto> getAllExperiencesByUserId(long userId);
    ExperienceDto getExperienceById(long experienceId);
    void deleteExperienceById(long experienceId, long userId, boolean isAdmin);
}
