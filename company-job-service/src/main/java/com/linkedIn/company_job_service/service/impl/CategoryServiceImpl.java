package com.linkedIn.company_job_service.service.impl;

import com.linkedIn.company_job_service.dto.category.CategoryDto;
import com.linkedIn.company_job_service.dto.category.CreateCategoryDto;
import com.linkedIn.company_job_service.entity.Category;
import com.linkedIn.company_job_service.exception.ApiException;
import com.linkedIn.company_job_service.repository.CategoryRepository;
import com.linkedIn.company_job_service.service.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setName(createCategoryDto.getName());

        saveAndCatchDuplicateNameError(category);
    }

    @Override
    public void updateCategoryById(CreateCategoryDto createCategoryDto, long categoryId) {
        Category category = returnCategoryOrThrowError(categoryId);
        category.setName(createCategoryDto.getName());

        saveAndCatchDuplicateNameError(category);
    }

    @Override
    public CategoryDto getCategoryById(long categoryId) {
        Category category = returnCategoryOrThrowError(categoryId);

        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCategoryById(long categoryId) {
        Category category = returnCategoryOrThrowError(categoryId);
        categoryRepository.delete(category);
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    private void saveAndCatchDuplicateNameError(Category category) {
        try {
            categoryRepository.save(category);
        }
        catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "category with this name already exists");
        }
    }

    private Category returnCategoryOrThrowError(long categoryId){
        return this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "category with id " + categoryId + " was not found"));
    }
}
