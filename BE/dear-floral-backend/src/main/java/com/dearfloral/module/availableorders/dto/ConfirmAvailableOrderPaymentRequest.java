package com.dearfloral.module.availableorders.dto;

import jakarta.validation.constraints.Size;

public record ConfirmAvailableOrderPaymentRequest(
        @Size(max = 100, message = "transactionRef must be at most 100 characters.")
        String transactionRef,

        @Size(max = 500, message = "paymentProofUrl must be at most 500 characters.")
        String paymentProofUrl
) {
}

