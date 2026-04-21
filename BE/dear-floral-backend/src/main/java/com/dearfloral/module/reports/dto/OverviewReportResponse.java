package com.dearfloral.module.reports.dto;

public record OverviewReportResponse(
        long totalProducts,
        long totalOrders,
        long processingOrders,
        long completedOrders
) {
}
