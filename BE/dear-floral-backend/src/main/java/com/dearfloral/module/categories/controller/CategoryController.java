package com.dearfloral.module.categories.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.module.categories.dto.CategoryResponse;
import com.dearfloral.module.categories.service.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> data = categoryService.getPublicCategories();
        return ResponseEntity.ok(ApiResponse.success("CATEGORY_LIST_FETCHED", "Category list fetched successfully.", data));
    }
}
