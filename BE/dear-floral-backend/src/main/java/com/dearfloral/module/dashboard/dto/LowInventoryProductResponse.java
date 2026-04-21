package com.dearfloral.module.dashboard.dto;

import com.dearfloral.common.enums.ProductKind;

public record LowInventoryProductResponse(
        Long productId,
        String productName,
        ProductKind productKind,
        Integer quantityOnHand
) {
}
