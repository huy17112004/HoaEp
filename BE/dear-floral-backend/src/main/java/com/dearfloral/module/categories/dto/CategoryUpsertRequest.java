package com.dearfloral.module.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpsertRequest(
        @NotBlank(message = "name is required.")
        @Size(max = 150, message = "name must be at most 150 characters.")
        String name,

        @Size(max = 255, message = "description must be at most 255 characters.")
        String description,

        @NotBlank(message = "status is required.")
        @Size(max = 50, message = "status must be at most 50 characters.")
        String status
) {
}
