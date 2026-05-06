package com.dearfloral.module.availableorders.dto;

import com.dearfloral.common.enums.AvailableOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AvailableOrderResponse(
        Long orderId,
        String orderCode,
        String shippingReceiverName,
        String shippingReceiverPhone,
        String shippingAddressLine,
        String shippingWard,
        String shippingDistrict,
        String shippingProvince,
        AvailableOrderStatus orderStatus,
        String paymentStatus,
        BigDecimal totalAmount,
        LocalDateTime orderedAt,
        List<AvailableOrderItemResponse> items,
        String rejectionReason,
        String refundBankName,
        String refundAccountNumber,
        String refundAccountName
) {
}
