package com.dearfloral.module.availableorders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateAvailableOrderItemRequest(
        @NotNull(message = "productId is required.")
        Long productId,

        @NotNull(message = "quantity is required.")
        @Min(value = 1, message = "quantity must be greater than 0.")
        Integer quantity
) {
}
