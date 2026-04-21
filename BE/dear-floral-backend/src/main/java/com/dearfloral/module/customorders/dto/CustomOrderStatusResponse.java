package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.CustomOrderStatus;

public record CustomOrderStatusResponse(
        Long orderId,
        String orderCode,
        CustomOrderStatus status
) {
}
