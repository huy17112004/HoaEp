package com.dearfloral.module.customorders.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.customorders.dto.CreateCustomDemoRequest;
import com.dearfloral.module.customorders.dto.CustomDeliveryResponse;
import com.dearfloral.module.customorders.dto.CustomDemoResponse;
import com.dearfloral.module.customorders.dto.CustomOrderResponse;
import com.dearfloral.module.customorders.dto.CustomOrderStatusResponse;
import com.dearfloral.module.customorders.dto.EvaluateFlowerInputRequest;
import com.dearfloral.module.customorders.dto.EvaluateFlowerInputResponse;
import com.dearfloral.module.customorders.dto.UpdateCustomDeliveryRequest;
import com.dearfloral.module.customorders.dto.UpdateCustomOrderStatusRequest;
import com.dearfloral.module.customorders.dto.VerifyDepositRequest;
import com.dearfloral.module.customorders.dto.VerifyRemainingPaymentRequest;
import com.dearfloral.module.customorders.service.CustomOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/admin/orders/custom")
public class AdminCustomOrderController {

    private final CustomOrderService customOrderService;

    public AdminCustomOrderController(CustomOrderService customOrderService) {
        this.customOrderService = customOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<CustomOrderResponse>>> getOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CustomOrderStatus orderStatus,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<CustomOrderResponse> data = customOrderService.getAdminOrders(
                keyword,
                orderStatus,
                paymentStatus,
                page,
                limit
        );
        PageMeta meta = customOrderService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_LIST_FETCHED", "Custom order list fetched successfully.", data.getContent(), meta));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> updateOrderStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateCustomOrderStatusRequest request
    ) {
        CustomOrderStatusResponse data = customOrderService.updateOrderStatus(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_STATUS_UPDATED", "Custom order status updated successfully.", data));
    }

    @PatchMapping("/{orderId}/verify-deposit")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> verifyDeposit(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @RequestBody VerifyDepositRequest request
    ) {
        CustomOrderStatusResponse data = customOrderService.verifyDeposit(orderId, request.accepted(), principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_DEPOSIT_VERIFIED", "Deposit verification processed successfully.", data));
    }

    @PatchMapping("/{orderId}/evaluate-flower-input")
    public ResponseEntity<ApiResponse<EvaluateFlowerInputResponse>> evaluateFlowerInput(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody EvaluateFlowerInputRequest request
    ) {
        EvaluateFlowerInputResponse data = customOrderService.evaluateFlowerInput(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "CUSTOM_ORDER_FLOWER_EVALUATED",
                "Flower input evaluated successfully.",
                data
        ));
    }

    @PatchMapping("/{orderId}/delivery")
    public ResponseEntity<ApiResponse<CustomDeliveryResponse>> updateDelivery(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateCustomDeliveryRequest request
    ) {
        CustomDeliveryResponse data = customOrderService.updateDelivery(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_DELIVERY_UPDATED", "Custom order delivery updated successfully.", data));
    }

    @PostMapping("/{orderId}/demos")
    public ResponseEntity<ApiResponse<CustomDemoResponse>> uploadDemo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @ModelAttribute CreateCustomDemoRequest request
    ) {
        CustomDemoResponse data = customOrderService.uploadDemo(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_DEMO_UPLOADED", "Custom demo uploaded successfully.", data));
    }

    @PatchMapping("/{orderId}/confirm-refund")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> confirmRefund(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        CustomOrderStatusResponse data = customOrderService.confirmRefund(orderId, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_REFUND_CONFIRMED", "Refund confirmed successfully.", data));
    }

    @PatchMapping("/{orderId}/verify-remaining-payment")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> verifyRemainingPayment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody VerifyRemainingPaymentRequest request
    ) {
        CustomOrderStatusResponse data = customOrderService.verifyRemainingPayment(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_REMAINING_PAYMENT_VERIFIED", "Remaining payment verification processed successfully.", data));
    }
}
