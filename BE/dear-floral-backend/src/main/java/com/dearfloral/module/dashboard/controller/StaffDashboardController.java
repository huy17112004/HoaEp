package com.dearfloral.module.dashboard.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.module.dashboard.dto.StaffDashboardResponse;
import com.dearfloral.module.dashboard.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/dashboard")
public class StaffDashboardController {

    private final DashboardService dashboardService;

    public StaffDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StaffDashboardResponse>> getDashboard() {
        StaffDashboardResponse data = dashboardService.getStaffDashboard();
        return ResponseEntity.ok(ApiResponse.success("STAFF_DASHBOARD_FETCHED", "Staff dashboard fetched successfully.", data));
    }
}
