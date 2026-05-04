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
import com.dearfloral.module.customorders.dto.CustomOrderStatusResponse;
import com.dearfloral.module.customorders.dto.DemoFeedbackRequest;
import com.dearfloral.module.customorders.dto.DemoFeedbackResponse;
import com.dearfloral.module.customorders.dto.RemainingPaymentResponse;
import com.dearfloral.module.customorders.dto.SubmitRefundInfoRequest;
import com.dearfloral.module.customorders.dto.UploadCustomFlowerImageResponse;
import com.dearfloral.module.customorders.service.CustomOrderService;
import com.dearfloral.module.products.service.LocalFileStorageService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orders/custom")
public class CustomOrderController {

    private final CustomOrderService customOrderService;
    private final LocalFileStorageService localFileStorageService;

    public CustomOrderController(
            CustomOrderService customOrderService,
            LocalFileStorageService localFileStorageService
    ) {
        this.customOrderService = customOrderService;
        this.localFileStorageService = localFileStorageService;
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

    @PostMapping("/{orderId}/confirm-deposit")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> confirmDeposit(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId
    ) {
        ensureCustomer(principal);
        CustomOrderStatusResponse data = customOrderService.confirmDeposit(orderId, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CUSTOM_ORDER_DEPOSIT_CONFIRMED", "Deposit transfer confirmed successfully.", data));
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

    @PostMapping("/{orderId}/refund-info")
    public ResponseEntity<ApiResponse<CustomOrderStatusResponse>> submitRefundInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody SubmitRefundInfoRequest request
    ) {
        ensureCustomer(principal);
        CustomOrderStatusResponse data = customOrderService.submitRefundInfo(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "CUSTOM_ORDER_REFUND_INFO_SUBMITTED",
                "Refund info submitted successfully.",
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

    @PostMapping("/flower-image")
    public ResponseEntity<ApiResponse<UploadCustomFlowerImageResponse>> uploadFlowerImage(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("file") MultipartFile file
    ) {
        ensureCustomer(principal);
        String imageUrl = localFileStorageService.saveProductImage(file);
        UploadCustomFlowerImageResponse data = new UploadCustomFlowerImageResponse(imageUrl);
        return ResponseEntity.ok(ApiResponse.success(
                "CUSTOM_FLOWER_IMAGE_UPLOADED",
                "Custom flower image uploaded successfully.",
                data
        ));
    }

    private void ensureCustomer(UserPrincipal principal) {
        if (!RoleCode.CUSTOMER.name().equals(principal.getRole())) {
            throw new AccessDeniedException("Customer role is required.");
        }
    }
}
