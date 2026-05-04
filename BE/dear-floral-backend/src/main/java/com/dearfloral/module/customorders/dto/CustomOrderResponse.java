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
        String shippingReceiverName,
        String shippingReceiverPhone,
        String shippingAddressLine,
        String shippingWard,
        String shippingDistrict,
        String shippingProvince,
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
        String rejectionReason,
        String refundBankName,
        String refundAccountNumber,
        String refundAccountName,
        Integer demoRevisionCount,
        BigDecimal extraRevisionFeeRate,
        LocalDateTime orderedAt
) {
}
