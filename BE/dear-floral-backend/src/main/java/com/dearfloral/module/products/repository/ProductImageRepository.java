package com.dearfloral.module.products.repository;

import com.dearfloral.module.products.entity.ProductImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    List<ProductImageEntity> findByProductIdOrderBySortOrderAsc(Long productId);
    void deleteByProductId(Long productId);
}
