package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.CustomOrderStatus;

public record DemoFeedbackResponse(
        CustomOrderStatus currentOrderStatus,
        Integer revisionCount
) {
}
