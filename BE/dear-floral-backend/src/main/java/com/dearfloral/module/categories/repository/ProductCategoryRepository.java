package com.dearfloral.module.categories.repository;

import com.dearfloral.module.categories.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
