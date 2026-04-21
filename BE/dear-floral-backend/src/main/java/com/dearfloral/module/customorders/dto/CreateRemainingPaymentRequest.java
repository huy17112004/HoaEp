package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRemainingPaymentRequest(
        @NotBlank(message = "paymentMethod is required.")
        @Size(max = 50, message = "paymentMethod must be at most 50 characters.")
        String paymentMethod,

        @Size(max = 100, message = "transactionRef must be at most 100 characters.")
        String transactionRef,

        @Size(max = 500, message = "paymentProof must be at most 500 characters.")
        String paymentProof
) {
}
