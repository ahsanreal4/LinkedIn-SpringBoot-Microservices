package com.linkedIn.company_job_service.controller;

import com.linkedIn.company_job_service.dto.category.CategoryDto;
import com.linkedIn.company_job_service.dto.category.CreateCategoryDto;
import com.linkedIn.company_job_service.service.CategoryService;
import com.linkedIn.company_job_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final UserUtils userUtils;
    private final CategoryService categoryService;

    public CategoryController(UserUtils userUtils, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public void createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto, HttpServletRequest request) {
        this.categoryService.createCategory(createCategoryDto);
    }

    @PutMapping("/{id}")
    public void updateCategoryById(@Valid @RequestBody CreateCategoryDto createCategoryDto, @PathVariable("id") long id, HttpServletRequest request) {
        userUtils.shouldBeAdmin(request);

        this.categoryService.updateCategoryById(createCategoryDto, id);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories(HttpServletRequest request) {
        List<CategoryDto> categories = this.categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long id, HttpServletRequest request) {
        CategoryDto categoryDto = this.categoryService.getCategoryById(id);

        return ResponseEntity.ok(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable("id") long id, HttpServletRequest request) {
        userUtils.shouldBeAdmin(request);

        this.categoryService.deleteCategoryById(id);
    }
}
