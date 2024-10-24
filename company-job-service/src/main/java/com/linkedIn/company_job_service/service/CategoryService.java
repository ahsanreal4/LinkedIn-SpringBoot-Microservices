package com.linkedIn.company_job_service.service;

import com.linkedIn.company_job_service.dto.category.CategoryDto;
import com.linkedIn.company_job_service.dto.category.CreateCategoryDto;

import java.util.List;

public interface CategoryService {
    void createCategory(CreateCategoryDto createCategoryDto);
    void updateCategoryById(CreateCategoryDto createCategoryDto, long categoryId);
    CategoryDto getCategoryById(long categoryId);
    List<CategoryDto> getAllCategories();
    void deleteCategoryById(long categoryId);
}
