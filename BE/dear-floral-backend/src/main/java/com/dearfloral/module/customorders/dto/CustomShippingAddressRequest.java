package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomShippingAddressRequest(
        @NotBlank(message = "receiverName is required.")
        @Size(max = 150, message = "receiverName must be at most 150 characters.")
        String receiverName,

        @NotBlank(message = "receiverPhone is required.")
        @Pattern(regexp = "^[0-9+\\-\\s]{8,20}$", message = "receiverPhone format is invalid.")
        String receiverPhone,

        @NotBlank(message = "addressLine is required.")
        @Size(max = 255, message = "addressLine must be at most 255 characters.")
        String addressLine,

        @NotBlank(message = "ward is required.")
        @Size(max = 100, message = "ward must be at most 100 characters.")
        String ward,

        @NotBlank(message = "district is required.")
        @Size(max = 100, message = "district must be at most 100 characters.")
        String district,

        @NotBlank(message = "province is required.")
        @Size(max = 100, message = "province must be at most 100 characters.")
        String province,

        @NotNull(message = "isDefault is required.")
        Boolean isDefault,

        @Size(max = 255, message = "note must be at most 255 characters.")
        String note
) {
}
