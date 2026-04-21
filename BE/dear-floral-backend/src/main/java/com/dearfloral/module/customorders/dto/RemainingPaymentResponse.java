package com.dearfloral.module.customorders.dto;

import java.math.BigDecimal;

public record RemainingPaymentResponse(
        String paymentStatus,
        BigDecimal remainingAmount
) {
}
