package com.dearfloral.module.customorders.dto;

public record CreateCustomOrderResponse(
        Long orderId,
        String orderCode,
        String depositStatus,
        String paymentStatus
) {
}
