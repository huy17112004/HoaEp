package com.dearfloral.module.inventory.dto;

import com.dearfloral.common.enums.ProductKind;

public record InventoryItemResponse(
        Long productId,
        String productName,
        ProductKind productKind,
        Boolean isSellableDirectly,
        Boolean isCustomSelectable,
        Integer quantityOnHand
) {
}
