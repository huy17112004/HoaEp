package com.dearfloral.module.delivery.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.delivery.dto.AvailableDeliveryResponse;
import com.dearfloral.module.delivery.dto.UpdateAvailableDeliveryRequest;
import com.dearfloral.module.delivery.service.AvailableOrderDeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders/available")
public class AdminAvailableOrderDeliveryController {

    private final AvailableOrderDeliveryService availableOrderDeliveryService;

    public AdminAvailableOrderDeliveryController(AvailableOrderDeliveryService availableOrderDeliveryService) {
        this.availableOrderDeliveryService = availableOrderDeliveryService;
    }

    @PatchMapping("/{orderId}/delivery")
    public ResponseEntity<ApiResponse<AvailableDeliveryResponse>> updateDelivery(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateAvailableDeliveryRequest request
    ) {
        AvailableDeliveryResponse data = availableOrderDeliveryService.updateDelivery(orderId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(
                "AVAILABLE_ORDER_DELIVERY_UPDATED",
                "Available order delivery updated successfully.",
                data
        ));
    }
}
