package com.dearfloral.module.users.dto;

import com.dearfloral.common.enums.RoleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAdminUserRequest(
        @NotBlank(message = "fullName is required.")
        @Size(max = 150, message = "fullName must be at most 150 characters.")
        String fullName,

        @NotBlank(message = "phone is required.")
        @Size(max = 20, message = "phone must be at most 20 characters.")
        String phone,

        @NotBlank(message = "email is required.")
        @Email(message = "email must be a valid email address.")
        @Size(max = 150, message = "email must be at most 150 characters.")
        String email,

        @NotBlank(message = "password is required.")
        @Size(min = 6, max = 100, message = "password must be between 6 and 100 characters.")
        String password,

        @NotNull(message = "role is required.")
        RoleCode role,

        @Size(max = 50, message = "status must be at most 50 characters.")
        String status
) {
}
