package com.dearfloral.module.delivery.dto;

import java.time.LocalDateTime;

public record AvailableDeliveryResponse(
        Long orderId,
        String orderCode,
        String deliveryStatus,
        String deliveryNote,
        LocalDateTime shippedTime,
        LocalDateTime deliveredTime
) {
}
