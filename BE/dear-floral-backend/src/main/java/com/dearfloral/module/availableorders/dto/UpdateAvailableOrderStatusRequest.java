package com.dearfloral.module.availableorders.dto;

import com.dearfloral.common.enums.AvailableOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAvailableOrderStatusRequest(
        @NotNull(message = "status is required.")
        AvailableOrderStatus status,

        @Size(max = 500, message = "reason must be at most 500 characters.")
        String reason
) {
}
