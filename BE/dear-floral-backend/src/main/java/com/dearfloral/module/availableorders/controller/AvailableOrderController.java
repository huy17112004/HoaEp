package com.dearfloral.module.availableorders.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.availableorders.dto.AvailableOrderResponse;
import com.dearfloral.module.availableorders.dto.CreateAvailableOrderRequest;
import com.dearfloral.module.availableorders.service.AvailableOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders/available")
public class AvailableOrderController {

    private final AvailableOrderService availableOrderService;

    public AvailableOrderController(AvailableOrderService availableOrderService) {
        this.availableOrderService = availableOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AvailableOrderResponse>> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateAvailableOrderRequest request
    ) {
        ensureCustomer(principal);
        AvailableOrderResponse data = availableOrderService.createOrder(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("AVAILABLE_ORDER_CREATED", "Available order created successfully.", data));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<Iterable<AvailableOrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        ensureCustomer(principal);
        Page<AvailableOrderResponse> data = availableOrderService.getMyOrders(principal.getUserId(), page, limit);
        PageMeta meta = availableOrderService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_LIST_FETCHED",
                "Available order list fetched successfully.",
                data.getContent(),
                meta
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<AvailableOrderResponse>> getOrderDetail(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        AvailableOrderResponse data = availableOrderService.getOrderDetail(
                orderId,
                principal.getUserId(),
                RoleCode.valueOf(principal.getRole())
        );
        return ResponseEntity.ok(ApiResponse.success("AVAILABLE_ORDER_DETAIL_FETCHED", "Available order detail fetched successfully.", data));
    }

    private void ensureCustomer(UserPrincipal principal) {
        if (!RoleCode.CUSTOMER.name().equals(principal.getRole())) {
            throw new AccessDeniedException("Customer role is required.");
        }
    }
}
