package com.dearfloral.module.reports.service;

import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.module.availableorders.repository.AvailableOrderPaymentRepository;
import com.dearfloral.module.availableorders.repository.AvailableOrderRepository;
import com.dearfloral.module.customorders.repository.CustomOrderPaymentRepository;
import com.dearfloral.module.customorders.repository.CustomOrderRepository;
import com.dearfloral.module.inventory.repository.InventoryItemRepository;
import com.dearfloral.module.products.repository.ProductRepository;
import com.dearfloral.module.reports.dto.InventoryReportItemResponse;
import com.dearfloral.module.reports.dto.OrderDomain;
import com.dearfloral.module.reports.dto.OrderStatisticItemResponse;
import com.dearfloral.module.reports.dto.OverviewReportResponse;
import com.dearfloral.module.reports.dto.ReportGroupBy;
import com.dearfloral.module.reports.dto.RevenueReportItemResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final AvailableOrderRepository availableOrderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final AvailableOrderPaymentRepository availableOrderPaymentRepository;
    private final CustomOrderPaymentRepository customOrderPaymentRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public ReportService(
            ProductRepository productRepository,
            AvailableOrderRepository availableOrderRepository,
            CustomOrderRepository customOrderRepository,
            AvailableOrderPaymentRepository availableOrderPaymentRepository,
            CustomOrderPaymentRepository customOrderPaymentRepository,
            InventoryItemRepository inventoryItemRepository
    ) {
        this.productRepository = productRepository;
        this.availableOrderRepository = availableOrderRepository;
        this.customOrderRepository = customOrderRepository;
        this.availableOrderPaymentRepository = availableOrderPaymentRepository;
        this.customOrderPaymentRepository = customOrderPaymentRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public OverviewReportResponse getOverview(LocalDate fromDate, LocalDate toDate) {
        DateRange range = toDateRange(fromDate, toDate);

        long totalProducts = productRepository.countInRange(range.from(), range.to());
        long totalOrders = availableOrderRepository.countInRange(range.from(), range.to())
                + customOrderRepository.countInRange(range.from(), range.to());

        long processingOrders = availableOrderRepository.countByStatusInRange(
                EnumSet.of(AvailableOrderStatus.RECEIVED, AvailableOrderStatus.PROCESSING, AvailableOrderStatus.SHIPPING),
                range.from(),
                range.to()
        ) + customOrderRepository.countByStatusInRange(
                EnumSet.of(
                        CustomOrderStatus.DEPOSITED,
                        CustomOrderStatus.WAITING_FLOWER_REVIEW,
                        CustomOrderStatus.IN_PROGRESS,
                        CustomOrderStatus.WAITING_DEMO_FEEDBACK,
                        CustomOrderStatus.WAITING_REMAINING_PAYMENT
                ),
                range.from(),
                range.to()
        );

        long completedOrders = availableOrderRepository.countByStatusInRange(
                EnumSet.of(AvailableOrderStatus.COMPLETED),
                range.from(),
                range.to()
        ) + customOrderRepository.countByStatusInRange(
                EnumSet.of(CustomOrderStatus.COMPLETED),
                range.from(),
                range.to()
        );

        return new OverviewReportResponse(totalProducts, totalOrders, processingOrders, completedOrders);
    }

    public List<RevenueReportItemResponse> getRevenue(LocalDate fromDate, LocalDate toDate, ReportGroupBy groupBy) {
        DateRange range = toDateRange(fromDate, toDate);

        Map<LocalDate, RevenueAggregation> merged = new HashMap<>();

        List<AvailableOrderPaymentRepository.RevenueBucketProjection> availableRevenueRows = groupBy == ReportGroupBy.MONTH
                ? availableOrderPaymentRepository.summarizeRevenueByMonth(range.from(), range.to())
                : availableOrderPaymentRepository.summarizeRevenueByDay(range.from(), range.to());
        availableRevenueRows.forEach(row -> {
            RevenueAggregation agg = merged.computeIfAbsent(row.getBucketDate(), k -> new RevenueAggregation());
            agg.availableRevenue = safeMoney(row.getRevenue());
        });

        List<CustomOrderPaymentRepository.RevenueBucketProjection> customRevenueRows = groupBy == ReportGroupBy.MONTH
                ? customOrderPaymentRepository.summarizeRevenueByMonth(range.from(), range.to())
                : customOrderPaymentRepository.summarizeRevenueByDay(range.from(), range.to());
        customRevenueRows.forEach(row -> {
            RevenueAggregation agg = merged.computeIfAbsent(row.getBucketDate(), k -> new RevenueAggregation());
            agg.customRevenue = safeMoney(row.getRevenue());
        });

        return merged.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new RevenueReportItemResponse(
                        entry.getKey(),
                        entry.getValue().availableRevenue,
                        entry.getValue().customRevenue,
                        entry.getValue().availableRevenue.add(entry.getValue().customRevenue)
                ))
                .toList();
    }

    public List<OrderStatisticItemResponse> getOrderStatistics(LocalDate fromDate, LocalDate toDate, OrderDomain orderDomain) {
        DateRange range = toDateRange(fromDate, toDate);
        List<OrderStatisticItemResponse> result = new ArrayList<>();

        if (orderDomain == OrderDomain.AVAILABLE || orderDomain == OrderDomain.ALL) {
            availableOrderRepository.summarizeOrderStatusInRange(range.from(), range.to())
                    .forEach(row -> result.add(new OrderStatisticItemResponse(
                            OrderDomain.AVAILABLE.name(),
                            row.getOrderStatus().name(),
                            row.getTotalOrders()
                    )));
        }

        if (orderDomain == OrderDomain.CUSTOM || orderDomain == OrderDomain.ALL) {
            customOrderRepository.summarizeOrderStatusInRange(range.from(), range.to())
                    .forEach(row -> result.add(new OrderStatisticItemResponse(
                            OrderDomain.CUSTOM.name(),
                            row.getOrderStatus().name(),
                            row.getTotalOrders()
                    )));
        }

        return result;
    }

    public List<InventoryReportItemResponse> getInventoryStatistics(ProductKind productKind) {
        return inventoryItemRepository.summarizeInventory(productKind).stream()
                .map(item -> new InventoryReportItemResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getProductKind(),
                        item.getQuantityOnHand(),
                        item.getUpdatedAt()
                ))
                .toList();
    }

    private DateRange toDateRange(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime from = fromDate == null ? LocalDate.of(1970, 1, 1).atStartOfDay() : fromDate.atStartOfDay();
        LocalDateTime to = toDate == null ? LocalDate.of(2999, 12, 31).atTime(23, 59, 59, 999_999_999) : toDate.plusDays(1).atStartOfDay().minusNanos(1);
        if (from != null && to != null && from.isAfter(to)) {
            throw new BusinessException("INVALID_DATE_RANGE", "fromDate must be before or equal to toDate.");
        }
        return new DateRange(from, to);
    }

    private BigDecimal safeMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private record DateRange(LocalDateTime from, LocalDateTime to) {
    }

    private static class RevenueAggregation {
        private BigDecimal availableRevenue = BigDecimal.ZERO;
        private BigDecimal customRevenue = BigDecimal.ZERO;
    }
}
