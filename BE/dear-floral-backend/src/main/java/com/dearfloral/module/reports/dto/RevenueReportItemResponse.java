package com.dearfloral.module.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueReportItemResponse(
        LocalDate bucketDate,
        BigDecimal availableRevenue,
        BigDecimal customRevenue,
        BigDecimal totalRevenue
) {
}
