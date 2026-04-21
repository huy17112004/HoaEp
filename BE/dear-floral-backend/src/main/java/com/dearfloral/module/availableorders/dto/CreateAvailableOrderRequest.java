package com.dearfloral.module.availableorders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateAvailableOrderRequest(
        @NotNull(message = "shippingAddressId is required.")
        Long shippingAddressId,

        @NotBlank(message = "paymentMethod is required.")
        @Size(max = 50, message = "paymentMethod must be at most 50 characters.")
        String paymentMethod,

        @Size(max = 100, message = "transactionRef must be at most 100 characters.")
        String transactionRef,

        @Size(max = 500, message = "paymentProofUrl must be at most 500 characters.")
        String paymentProofUrl,

        @Size(max = 500, message = "note must be at most 500 characters.")
        String note,

        @NotEmpty(message = "items must not be empty.")
        List<@Valid CreateAvailableOrderItemRequest> items
) {
}
