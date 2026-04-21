package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.DeliveryType;
import java.time.LocalDateTime;

public record CustomDeliveryResponse(
        Long orderId,
        String orderCode,
        DeliveryType deliveryType,
        String deliveryStatus,
        String deliveryNote,
        LocalDateTime pickupTime,
        LocalDateTime shippedTime,
        LocalDateTime deliveredTime
) {
}
