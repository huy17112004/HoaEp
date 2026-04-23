package com.dearfloral.module.delivery.dto;

import java.time.LocalDateTime;

public record DeliveryTrackingResponse(
        String trackingType,
        Long trackingRecordId,
        Long orderId,
        String orderCode,
        String deliveryType,
        String deliveryStatus,
        String customerAddress,
        LocalDateTime orderedAt,
        LocalDateTime eventTime,
        String receiverNote
) {
}
