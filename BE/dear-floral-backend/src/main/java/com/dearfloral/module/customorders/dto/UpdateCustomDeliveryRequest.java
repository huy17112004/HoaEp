package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.DeliveryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UpdateCustomDeliveryRequest(
        @NotNull(message = "deliveryType is required.")
        DeliveryType deliveryType,

        @NotBlank(message = "deliveryStatus is required.")
        @Size(max = 50, message = "deliveryStatus must be at most 50 characters.")
        String deliveryStatus,

        @Size(max = 500, message = "deliveryNote must be at most 500 characters.")
        String deliveryNote,

        LocalDateTime deliveryTime
) {
}
