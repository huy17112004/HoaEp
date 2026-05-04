package com.dearfloral.module.dashboard.service;

import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.DemoResponseStatus;
import com.dearfloral.module.availableorders.repository.AvailableOrderRepository;
import com.dearfloral.module.customorders.repository.CustomDemoRepository;
import com.dearfloral.module.customorders.repository.CustomOrderRepository;
import com.dearfloral.module.dashboard.dto.LowInventoryProductResponse;
import com.dearfloral.module.dashboard.dto.RecentOrderResponse;
import com.dearfloral.module.dashboard.dto.StaffDashboardResponse;
import com.dearfloral.module.inventory.repository.InventoryItemRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private static final int LOW_INVENTORY_THRESHOLD = 5;
    private static final int MAX_RECENT_ORDERS = 10;

    private final AvailableOrderRepository availableOrderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final CustomDemoRepository customDemoRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public DashboardService(
            AvailableOrderRepository availableOrderRepository,
            CustomOrderRepository customOrderRepository,
            CustomDemoRepository customDemoRepository,
            InventoryItemRepository inventoryItemRepository
    ) {
        this.availableOrderRepository = availableOrderRepository;
        this.customOrderRepository = customOrderRepository;
        this.customDemoRepository = customDemoRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public StaffDashboardResponse getStaffDashboard() {
        long pendingAvailableOrders = availableOrderRepository.countByOrderStatus(AvailableOrderStatus.RECEIVED);
        long pendingCustomOrders = customOrderRepository.countByOrderStatus(CustomOrderStatus.WAITING_FLOWER_REVIEW);
        long demosPendingApproval = customDemoRepository.countByCustomerResponseStatus(DemoResponseStatus.PENDING);

        List<LowInventoryProductResponse> lowInventoryProducts = inventoryItemRepository
                .findLowInventoryProducts(LOW_INVENTORY_THRESHOLD)
                .stream()
                .map(item -> new LowInventoryProductResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getProductKind(),
                        item.getQuantityOnHand()
                ))
                .toList();

        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay().minusNanos(1);
        List<RecentOrderResponse> recentOrdersToday = getRecentOrders(from, to);

        return new StaffDashboardResponse(
                pendingAvailableOrders,
                pendingCustomOrders,
                demosPendingApproval,
                LOW_INVENTORY_THRESHOLD,
                lowInventoryProducts,
                recentOrdersToday
        );
    }

    private List<RecentOrderResponse> getRecentOrders(LocalDateTime from, LocalDateTime to) {
        List<RecentOrderResponse> merged = new ArrayList<>();
        availableOrderRepository.findRecentOrdersInRange(from, to).forEach(order -> merged.add(
                new RecentOrderResponse(
                        "AVAILABLE",
                        order.getOrderId(),
                        order.getOrderCode(),
                        order.getOrderStatus().name(),
                        order.getTotalAmount(),
                        order.getOrderedAt()
                )
        ));
        customOrderRepository.findRecentOrdersInRange(from, to).forEach(order -> merged.add(
                new RecentOrderResponse(
                        "CUSTOM",
                        order.getOrderId(),
                        order.getOrderCode(),
                        order.getOrderStatus().name(),
                        order.getTotalAmount(),
                        order.getOrderedAt()
                )
        ));

        return merged.stream()
                .sorted(Comparator.comparing(RecentOrderResponse::orderedAt).reversed())
                .limit(MAX_RECENT_ORDERS)
                .toList();
    }
}
