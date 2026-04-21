package com.dearfloral.module.auth.dto;

public record UserProfileResponse(
        Long userId,
        String fullName,
        String phone,
        String email,
        String role,
        String status
) {
}
