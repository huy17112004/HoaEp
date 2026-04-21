package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomDemoRequest(
        @NotBlank(message = "demoImage is required.")
        @Size(max = 500, message = "demoImage must be at most 500 characters.")
        String demoImage,

        @Size(max = 1000, message = "demoDescription must be at most 1000 characters.")
        String demoDescription
) {
}
