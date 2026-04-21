package com.dearfloral.module.customorders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateCustomOrderRequest(
        Long shippingAddressId,

        @Valid
        CustomShippingAddressRequest shippingAddressObject,

        @NotNull(message = "selectedFrameProductId is required.")
        Long selectedFrameProductId,

        @NotBlank(message = "flowerType is required.")
        @Size(max = 100, message = "flowerType must be at most 100 characters.")
        String flowerType,

        @Size(max = 2000, message = "personalizationContent must be at most 2000 characters.")
        String personalizationContent,

        @Future(message = "requestedDeliveryDate must be in the future.")
        LocalDate requestedDeliveryDate,

        @NotBlank(message = "flowerInputImage is required.")
        @Size(max = 500, message = "flowerInputImage must be at most 500 characters.")
        String flowerInputImage,

        @NotBlank(message = "depositPaymentMethod is required.")
        @Size(max = 50, message = "depositPaymentMethod must be at most 50 characters.")
        String depositPaymentMethod,

        @Size(max = 100, message = "depositTransactionRef must be at most 100 characters.")
        String depositTransactionRef,

        @Size(max = 500, message = "depositPaymentProof must be at most 500 characters.")
        String depositPaymentProof,

        @Size(max = 500, message = "note must be at most 500 characters.")
        String note
) {
}
