package com.dearfloral.module.reports.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.module.reports.dto.InventoryReportItemResponse;
import com.dearfloral.module.reports.dto.OrderDomain;
import com.dearfloral.module.reports.dto.OrderStatisticItemResponse;
import com.dearfloral.module.reports.dto.OverviewReportResponse;
import com.dearfloral.module.reports.dto.ReportGroupBy;
import com.dearfloral.module.reports.dto.RevenueReportItemResponse;
import com.dearfloral.module.reports.service.ReportService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<OverviewReportResponse>> getOverview(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {
        OverviewReportResponse data = reportService.getOverview(fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("REPORT_OVERVIEW_FETCHED", "Overview report fetched successfully.", data));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<List<RevenueReportItemResponse>>> getRevenue(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "DAY") ReportGroupBy groupBy
    ) {
        List<RevenueReportItemResponse> data = reportService.getRevenue(fromDate, toDate, groupBy);
        return ResponseEntity.ok(ApiResponse.success("REPORT_REVENUE_FETCHED", "Revenue report fetched successfully.", data));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderStatisticItemResponse>>> getOrderStatistics(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "ALL") OrderDomain orderDomain
    ) {
        List<OrderStatisticItemResponse> data = reportService.getOrderStatistics(fromDate, toDate, orderDomain);
        return ResponseEntity.ok(ApiResponse.success("REPORT_ORDERS_FETCHED", "Order statistics fetched successfully.", data));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<List<InventoryReportItemResponse>>> getInventoryStatistics(
            @RequestParam(required = false) ProductKind productKind
    ) {
        List<InventoryReportItemResponse> data = reportService.getInventoryStatistics(productKind);
        return ResponseEntity.ok(ApiResponse.success("REPORT_INVENTORY_FETCHED", "Inventory statistics fetched successfully.", data));
    }
}
