package com.dearfloral.module.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UpdateAvailableDeliveryRequest(
        @NotBlank(message = "deliveryStatus is required.")
        @Size(max = 50, message = "deliveryStatus must be at most 50 characters.")
        String deliveryStatus,

        @Size(max = 500, message = "deliveryNote must be at most 500 characters.")
        String deliveryNote,

        LocalDateTime deliveryTime
) {
}
