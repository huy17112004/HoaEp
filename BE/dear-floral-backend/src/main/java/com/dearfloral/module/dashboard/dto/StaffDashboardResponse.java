package com.dearfloral.module.dashboard.dto;

import java.util.List;

public record StaffDashboardResponse(
        long pendingAvailableOrders,
        long pendingCustomOrders,
        long demosPendingApproval,
        List<LowInventoryProductResponse> lowInventoryProducts,
        List<RecentOrderResponse> recentOrdersToday
) {
}
