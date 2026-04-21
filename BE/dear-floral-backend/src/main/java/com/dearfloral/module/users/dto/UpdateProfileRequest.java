package com.dearfloral.module.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "fullName is required.")
        @Size(max = 150, message = "fullName must be at most 150 characters.")
        String fullName,

        @NotBlank(message = "phone is required.")
        @Pattern(regexp = "^[0-9+\\-\\s]{8,20}$", message = "phone format is invalid.")
        String phone,

        @NotBlank(message = "email is required.")
        @Email(message = "email format is invalid.")
        @Size(max = 150, message = "email must be at most 150 characters.")
        String email
) {
}
