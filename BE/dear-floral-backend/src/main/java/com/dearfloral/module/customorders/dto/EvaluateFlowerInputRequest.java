package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.FlowerEvaluationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EvaluateFlowerInputRequest(
        @NotNull(message = "evaluationStatus is required.")
        FlowerEvaluationStatus evaluationStatus,

        @Size(max = 500, message = "evaluationNote must be at most 500 characters.")
        String evaluationNote
) {
}
