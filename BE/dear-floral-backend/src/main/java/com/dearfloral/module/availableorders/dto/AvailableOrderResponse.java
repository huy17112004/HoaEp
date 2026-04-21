package com.dearfloral.module.availableorders.dto;

import com.dearfloral.common.enums.AvailableOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AvailableOrderResponse(
        Long orderId,
        String orderCode,
        AvailableOrderStatus orderStatus,
        String paymentStatus,
        BigDecimal totalAmount,
        LocalDateTime orderedAt,
        List<AvailableOrderItemResponse> items
) {
}
