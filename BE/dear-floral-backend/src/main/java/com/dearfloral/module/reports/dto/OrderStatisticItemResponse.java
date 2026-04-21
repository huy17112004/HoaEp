package com.dearfloral.module.reports.dto;

public record OrderStatisticItemResponse(
        String orderDomain,
        String orderStatus,
        long totalOrders
) {
}
