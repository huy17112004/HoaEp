package com.dearfloral.module.availableorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmitAvailableOrderShippingInfoRequest(
        @NotBlank(message = "shippingCarrier is required.")
        @Size(max = 150, message = "shippingCarrier must be at most 150 characters.")
        String shippingCarrier,

        @NotBlank(message = "shippingTrackingCode is required.")
        @Size(max = 100, message = "shippingTrackingCode must be at most 100 characters.")
        String shippingTrackingCode
) {
}

