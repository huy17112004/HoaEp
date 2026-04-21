package com.dearfloral.module.purchasereceipts.dto;

import java.math.BigDecimal;

public record PurchaseReceiptItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitCost,
        BigDecimal subtotal
) {
}
