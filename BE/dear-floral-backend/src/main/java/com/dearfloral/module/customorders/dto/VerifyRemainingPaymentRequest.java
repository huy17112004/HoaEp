package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VerifyRemainingPaymentRequest(
        @NotNull(message = "received is required.")
        Boolean received,

        @Size(max = 500, message = "note must be at most 500 characters.")
        String note
) {
}
