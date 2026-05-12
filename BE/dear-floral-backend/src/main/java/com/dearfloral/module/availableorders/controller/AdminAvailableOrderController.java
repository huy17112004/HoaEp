package com.dearfloral.module.availableorders.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.availableorders.dto.AvailableOrderResponse;
import com.dearfloral.module.availableorders.dto.AvailableOrderStatusResponse;
import com.dearfloral.module.availableorders.dto.SubmitAvailableOrderShippingInfoRequest;
import com.dearfloral.module.availableorders.dto.UpdateAvailableOrderStatusRequest;
import com.dearfloral.module.availableorders.dto.VerifyAvailableOrderPaymentRequest;
import com.dearfloral.module.availableorders.service.AvailableOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders/available")
public class AdminAvailableOrderController {

    private final AvailableOrderService availableOrderService;

    public AdminAvailableOrderController(AvailableOrderService availableOrderService) {
        this.availableOrderService = availableOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<AvailableOrderResponse>>> getOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) AvailableOrderStatus orderStatus,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<AvailableOrderResponse> data = availableOrderService.getAdminOrders(keyword, orderStatus, paymentStatus, page, limit);
        PageMeta meta = availableOrderService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_LIST_FETCHED",
                "Available order list fetched successfully.",
                data.getContent(),
                meta
        ));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<AvailableOrderStatusResponse>> updateOrderStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateAvailableOrderStatusRequest request
    ) {
        AvailableOrderStatusResponse data = availableOrderService.updateOrderStatus(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_STATUS_UPDATED",
                "Available order status updated successfully.",
                data
        ));
    }

    @PatchMapping("/{orderId}/verify-payment")
    public ResponseEntity<ApiResponse<AvailableOrderStatusResponse>> verifyPayment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody VerifyAvailableOrderPaymentRequest request
    ) {
        AvailableOrderStatusResponse data = availableOrderService.verifyPayment(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_PAYMENT_VERIFIED",
                "Available order payment verification processed successfully.",
                data
        ));
    }

    @PatchMapping("/{orderId}/confirm-refund")
    public ResponseEntity<ApiResponse<AvailableOrderStatusResponse>> confirmRefund(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        AvailableOrderStatusResponse data = availableOrderService.confirmRefund(orderId, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_REFUND_CONFIRMED",
                "Available order refund confirmed successfully.",
                data
        ));
    }

    @PatchMapping("/{orderId}/submit-shipping")
    public ResponseEntity<ApiResponse<AvailableOrderStatusResponse>> submitShippingInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody SubmitAvailableOrderShippingInfoRequest request
    ) {
        AvailableOrderStatusResponse data = availableOrderService.submitShippingInfo(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_SHIPPING_SUBMITTED",
                "Available order shipping info submitted successfully.",
                data
        ));
    }
}
