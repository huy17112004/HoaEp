package com.dearfloral.module.availableorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmitAvailableOrderRefundInfoRequest(
        @NotBlank(message = "refundBankName is required.")
        @Size(max = 150, message = "refundBankName must be at most 150 characters.")
        String refundBankName,

        @NotBlank(message = "refundAccountNumber is required.")
        @Size(max = 50, message = "refundAccountNumber must be at most 50 characters.")
        String refundAccountNumber,

        @NotBlank(message = "refundAccountName is required.")
        @Size(max = 150, message = "refundAccountName must be at most 150 characters.")
        String refundAccountName
) {
}

