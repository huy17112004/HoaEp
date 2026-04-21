package com.dearfloral.module.availableorders.dto;

import com.dearfloral.common.enums.AvailableOrderStatus;

public record AvailableOrderStatusResponse(
        Long orderId,
        String orderCode,
        AvailableOrderStatus status
) {
}
