package com.dearfloral.module.purchasereceipts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PurchaseReceiptItemRequest(
        @NotNull(message = "productId is required.")
        Long productId,

        @NotNull(message = "quantity is required.")
        @Min(value = 1, message = "quantity must be greater than 0.")
        Integer quantity,

        @NotNull(message = "unitCost is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "unitCost must be greater than 0.")
        BigDecimal unitCost
) {
}
