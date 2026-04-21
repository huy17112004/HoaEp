package com.dearfloral.module.availableorders.dto;

import java.math.BigDecimal;

public record AvailableOrderItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
