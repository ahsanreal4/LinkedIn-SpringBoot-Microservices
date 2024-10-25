package com.linkedIn.users_service.controller;

import com.linkedIn.users_service.dto.experience.CreateExperienceDto;
import com.linkedIn.users_service.dto.experience.ExperienceDto;
import com.linkedIn.users_service.dto.experience.UpdateExperienceDto;
import com.linkedIn.users_service.service.ExperienceService;
import com.linkedIn.users_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/experiences")
public class ExperienceController {

    private final UserUtils userUtils;
    private final ExperienceService experienceService;

    public ExperienceController(UserUtils userUtils, ExperienceService experienceService) {
        this.userUtils = userUtils;
        this.experienceService = experienceService;
    }

    @PostMapping("")
    public void createExperience(@Valid @RequestBody CreateExperienceDto createExperienceDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        this.experienceService.createExperience(createExperienceDto, userId);
    }

    @PutMapping("/{id}")
    public void updateExperience(@Valid @RequestBody UpdateExperienceDto updateExperienceDto, @PathVariable("id") long id, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        this.experienceService.updateExperienceDto(updateExperienceDto, id, userId);
    }

    @GetMapping("")
    public ResponseEntity<List<ExperienceDto>> getAllExperiences(HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        List<ExperienceDto> experiences = this.experienceService.getAllExperiencesByUserId(userId);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceDto> getExperienceById(@PathVariable("id") long id) {
        ExperienceDto experience = this.experienceService.getExperienceById(id);

        return ResponseEntity.ok(experience);
    }

    @DeleteMapping("/{id}")
    public void deleteExperienceById(@PathVariable("id") long id, HttpServletRequest request) {
        boolean isAdmin = userUtils.isAdmin(request);
        long userId = userUtils.getUserId(request);

        this.experienceService.deleteExperienceById(id, userId, isAdmin);
    }
}
