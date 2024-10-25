package com.linkedIn.users_service.service.impl;

import com.linkedIn.users_service.dto.experience.CreateExperienceDto;
import com.linkedIn.users_service.dto.experience.ExperienceDto;
import com.linkedIn.users_service.dto.experience.UpdateExperienceDto;
import com.linkedIn.users_service.entity.User;
import com.linkedIn.users_service.exception.ApiException;
import com.linkedIn.users_service.repository.ExperienceRepository;
import com.linkedIn.users_service.service.ExperienceService;
import com.linkedIn.users_service.entity.Experience;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public void createExperience(CreateExperienceDto createExperienceDto, long userId) {
        User user = new User();
        user.setId(userId);

        Experience experience = new Experience();
        experience.setUser(user);
        experience.setDescription(createExperienceDto.getDescription());
        experience.setExperienceInYears(createExperienceDto.getExperienceInYears());
        experience.setCompany(createExperienceDto.getCompany());
        experience.setPosition(createExperienceDto.getPosition());
        experience.setStartDate(createExperienceDto.getStartDate());
        experience.setEndDate(createExperienceDto.getEndDate());

        experienceRepository.save(experience);
    }

    @Override
    public void updateExperienceDto(UpdateExperienceDto updateExperienceDto, long experienceId, long userId) {
        Experience experience = returnExperienceOrThrowError(experienceId);

        if(!experience.getUser().getId().equals(userId)) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot update someone else's experience");

        if(updateExperienceDto.getExperienceInYears() != null) experience.setExperienceInYears(updateExperienceDto.getExperienceInYears());
        if(updateExperienceDto.getCompany() != null) experience.setCompany(updateExperienceDto.getCompany());
        if(updateExperienceDto.getDescription() != null) experience.setDescription(updateExperienceDto.getDescription());
        if(updateExperienceDto.getPosition() != null) experience.setPosition(updateExperienceDto.getPosition());
        if(updateExperienceDto.getStartDate() != null) experience.setStartDate(updateExperienceDto.getStartDate());
        if(updateExperienceDto.getEndDate() != null) experience.setEndDate(updateExperienceDto.getEndDate());

        experienceRepository.save(experience);
    }

    @Override
    public List<ExperienceDto> getAllExperiencesByUserId(long userId) {
        List<Experience> experiences = experienceRepository.findAll();

        return experiences.stream().map(this::mapToExperienceDto).collect(Collectors.toList());
    }

    @Override
    public ExperienceDto getExperienceById(long experienceId) {
        Experience experience = returnExperienceOrThrowError(experienceId);

        return mapToExperienceDto(experience);
    }

    @Override
    public void deleteExperienceById(long experienceId, long userId, boolean isAdmin) {
        Experience experience = returnExperienceOrThrowError(experienceId);

        if(!experience.getUser().getId().equals(userId) && !isAdmin) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot delete someone else experience");

        experienceRepository.delete(experience);
    }

    private ExperienceDto mapToExperienceDto(Experience experience) {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(experience.getId());
        dto.setExperienceInYears(experience.getExperienceInYears());
        dto.setStartDate(experience.getStartDate());
        dto.setEndDate(experience.getEndDate());
        dto.setDescription(experience.getDescription());
        dto.setCompany(experience.getCompany());
        dto.setPosition(experience.getPosition());
        dto.setUserId(experience.getUser().getId());

        return dto;
    }

    private Experience returnExperienceOrThrowError(long experienceId) {
        return experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user experience with id " + experienceId + " was not found"));
    }
}
