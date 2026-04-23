package com.dearfloral.module.delivery.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.module.delivery.dto.DeliveryTrackingResponse;
import com.dearfloral.module.delivery.service.DeliveryTrackingService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/delivery-tracking")
public class StaffDeliveryTrackingController {

    private final DeliveryTrackingService deliveryTrackingService;

    public StaffDeliveryTrackingController(DeliveryTrackingService deliveryTrackingService) {
        this.deliveryTrackingService = deliveryTrackingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryTrackingResponse>>> getTrackingRecords(
            @RequestParam(required = false) String deliveryStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<DeliveryTrackingResponse> data = deliveryTrackingService.getTrackingRecords(deliveryStatus, page, limit);
        PageMeta meta = deliveryTrackingService.toPageMeta(deliveryStatus, page, limit);
        return ResponseEntity.ok(ApiResponse.success(
                "DELIVERY_TRACKING_FETCHED",
                "Delivery tracking records fetched successfully.",
                data,
                meta
        ));
    }
}
