package com.dearfloral.module.customorders.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.customorders.dto.CreateCustomOrderRequest;
import com.dearfloral.module.customorders.dto.CreateCustomOrderResponse;
import com.dearfloral.module.customorders.dto.CreateRemainingPaymentRequest;
import com.dearfloral.module.customorders.dto.CustomDemoResponse;
import com.dearfloral.module.customorders.dto.CustomOrderResponse;
import com.dearfloral.module.customorders.dto.DemoFeedbackRequest;
import com.dearfloral.module.customorders.dto.DemoFeedbackResponse;
import com.dearfloral.module.customorders.dto.RemainingPaymentResponse;
import com.dearfloral.module.customorders.service.CustomOrderService;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/orders/custom")
public class CustomOrderController {

    private final CustomOrderService customOrderService;

    public CustomOrderController(CustomOrderService customOrderService) {
        this.customOrderService = customOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateCustomOrderResponse>> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateCustomOrderRequest request
    ) {
        ensureCustomer(principal);
        CreateCustomOrderResponse data = customOrderService.createOrder(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_CREATED", "Custom order created successfully.", data));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<Iterable<CustomOrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        ensureCustomer(principal);
        Page<CustomOrderResponse> data = customOrderService.getMyOrders(principal.getUserId(), page, limit);
        PageMeta meta = customOrderService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_LIST_FETCHED", "Custom order list fetched successfully.", data.getContent(), meta));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<CustomOrderResponse>> getOrderDetail(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        CustomOrderResponse data = customOrderService.getOrderDetail(orderId, principal.getUserId(), RoleCode.valueOf(principal.getRole()));
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_DETAIL_FETCHED", "Custom order detail fetched successfully.", data));
    }

    @PostMapping("/{orderId}/remaining-payment")
    public ResponseEntity<ApiResponse<RemainingPaymentResponse>> payRemaining(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody CreateRemainingPaymentRequest request
    ) {
        ensureCustomer(principal);
        RemainingPaymentResponse data = customOrderService.payRemaining(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "CUSTOM_ORDER_REMAINING_PAYMENT_COMPLETED",
                "Remaining payment completed successfully.",
                data
        ));
    }

    @GetMapping("/{orderId}/demos")
    public ResponseEntity<ApiResponse<List<CustomDemoResponse>>> getDemos(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        List<CustomDemoResponse> data = customOrderService.getOrderDemos(
                orderId,
                principal.getUserId(),
                RoleCode.valueOf(principal.getRole())
        );
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_DEMO_LIST_FETCHED", "Custom demo list fetched successfully.", data));
    }

    @PostMapping("/{orderId}/demos/{demoId}/feedback")
    public ResponseEntity<ApiResponse<DemoFeedbackResponse>> feedbackDemo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @PathVariable Long demoId,
            @Valid @RequestBody DemoFeedbackRequest request
    ) {
        ensureCustomer(principal);
        DemoFeedbackResponse data = customOrderService.feedbackDemo(orderId, demoId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_DEMO_FEEDBACK_SUBMITTED", "Demo feedback submitted successfully.", data));
    }

    private void ensureCustomer(UserPrincipal principal) {
        if (!RoleCode.CUSTOMER.name().equals(principal.getRole())) {
            throw new AccessDeniedException("Customer role is required.");
        }
    }
}
