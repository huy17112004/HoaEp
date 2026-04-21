package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.CustomOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCustomOrderStatusRequest(
        @NotNull(message = "status is required.")
        CustomOrderStatus status,

        @Size(max = 500, message = "reason must be at most 500 characters.")
        String reason
) {
}
