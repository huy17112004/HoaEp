package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.FlowerEvaluationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomOrderResponse(
        Long id,
        String orderCode,
        Long selectedFrameProductId,
        String selectedFrameName,
        CustomOrderStatus orderStatus,
        String paymentStatus,
        BigDecimal depositAmount,
        BigDecimal remainingAmount,
        BigDecimal totalAmount,
        String flowerType,
        String personalizationContent,
        LocalDate requestedDeliveryDate,
        String flowerInputImageUrl,
        FlowerEvaluationStatus flowerEvaluationStatus,
        String flowerEvaluationNote,
        Integer demoRevisionCount,
        BigDecimal extraRevisionFeeRate,
        LocalDateTime orderedAt
) {
}
