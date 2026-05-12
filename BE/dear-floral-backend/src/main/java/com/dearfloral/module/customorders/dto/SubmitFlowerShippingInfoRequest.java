package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmitFlowerShippingInfoRequest(
        @NotBlank(message = "carrier is required.")
        @Size(max = 150, message = "carrier must be at most 150 characters.")
        String carrier,

        @NotBlank(message = "trackingCode is required.")
        @Size(max = 100, message = "trackingCode must be at most 100 characters.")
        String trackingCode
) {
}

