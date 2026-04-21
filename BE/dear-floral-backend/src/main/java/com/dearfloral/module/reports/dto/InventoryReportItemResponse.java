package com.dearfloral.module.reports.dto;

import com.dearfloral.common.enums.ProductKind;
import java.time.LocalDateTime;

public record InventoryReportItemResponse(
        Long productId,
        String productName,
        ProductKind productKind,
        Integer quantityOnHand,
        LocalDateTime updatedAt
) {
}
