package com.dearfloral.module.categories.service;

import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.categories.dto.CategoryResponse;
import com.dearfloral.module.categories.dto.CategoryUpsertRequest;
import com.dearfloral.module.categories.entity.ProductCategoryEntity;
import com.dearfloral.module.categories.repository.ProductCategoryRepository;
import com.dearfloral.module.products.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(
            ProductCategoryRepository productCategoryRepository,
            ProductRepository productRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> getPublicCategories() {
        return productCategoryRepository.findAll().stream()
                .filter(category -> "ACTIVE".equalsIgnoreCase(category.getStatus()))
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> getAdminCategories() {
        return productCategoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public CategoryResponse create(CategoryUpsertRequest request) {
        if (productCategoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new BusinessException("CATEGORY_NAME_EXISTS", "Category name already exists.");
        }
        ProductCategoryEntity entity = new ProductCategoryEntity();
        applyRequest(entity, request);
        return toResponse(productCategoryRepository.save(entity));
    }

    @Transactional
    public CategoryResponse update(Long categoryId, CategoryUpsertRequest request) {
        ProductCategoryEntity entity = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND", "Category not found."));
        if (productCategoryRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), categoryId)) {
            throw new BusinessException("CATEGORY_NAME_EXISTS", "Category name already exists.");
        }
        applyRequest(entity, request);
        return toResponse(productCategoryRepository.save(entity));
    }

    @Transactional
    public void delete(Long categoryId) {
        ProductCategoryEntity entity = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND", "Category not found."));
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new BusinessException(
                    "CATEGORY_HAS_PRODUCTS",
                    "Cannot delete category because there are products assigned to it."
            );
        }
        entity.setStatus("INACTIVE");
        productCategoryRepository.save(entity);
    }

    private void applyRequest(ProductCategoryEntity entity, CategoryUpsertRequest request) {
        entity.setName(request.name().trim());
        entity.setDescription(request.description() == null ? null : request.description().trim());
        entity.setStatus(request.status().trim().toUpperCase());
    }

    private CategoryResponse toResponse(ProductCategoryEntity entity) {
        return new CategoryResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus());
    }
}
