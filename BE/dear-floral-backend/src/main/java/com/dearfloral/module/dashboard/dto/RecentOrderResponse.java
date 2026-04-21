package com.dearfloral.module.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecentOrderResponse(
        String orderDomain,
        Long orderId,
        String orderCode,
        String orderStatus,
        BigDecimal totalAmount,
        LocalDateTime orderedAt
) {
}
