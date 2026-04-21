package com.dearfloral.module.products.dto;

import com.dearfloral.common.enums.ProductKind;
import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        Long categoryId,
        String categoryName,
        String name,
        String slug,
        String description,
        BigDecimal price,
        ProductKind productKind,
        Boolean isSellableDirectly,
        Boolean isCustomSelectable,
        String imageUrl,
        String size,
        String material,
        String flowerType,
        String status
) {
}
