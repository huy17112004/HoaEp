package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.FlowerEvaluationStatus;

public record EvaluateFlowerInputResponse(
        Long orderId,
        FlowerEvaluationStatus flowerEvaluationStatus,
        CustomOrderStatus nextStep
) {
}
