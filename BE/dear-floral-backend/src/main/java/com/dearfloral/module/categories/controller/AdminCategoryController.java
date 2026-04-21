package com.dearfloral.module.categories.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.module.categories.dto.CategoryResponse;
import com.dearfloral.module.categories.dto.CategoryUpsertRequest;
import com.dearfloral.module.categories.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> data = categoryService.getAdminCategories();
        return ResponseEntity.ok(ApiResponse.success("CATEGORY_LIST_FETCHED", "Category list fetched successfully.", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryUpsertRequest request) {
        CategoryResponse data = categoryService.create(request);
        return ResponseEntity.ok(ApiResponse.success("CATEGORY_CREATED", "Category created successfully.", data));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpsertRequest request
    ) {
        CategoryResponse data = categoryService.update(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success("CATEGORY_UPDATED", "Category updated successfully.", data));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok(ApiResponse.success("CATEGORY_DELETED", "Category deleted successfully.", null));
    }
}
