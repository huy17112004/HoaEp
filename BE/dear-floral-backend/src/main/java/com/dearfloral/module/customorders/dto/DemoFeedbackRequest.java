package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DemoFeedbackRequest(
        @NotNull(message = "action is required.")
        DemoFeedbackAction action,

        @Size(max = 1000, message = "feedback must be at most 1000 characters.")
        String feedback
) {
}
