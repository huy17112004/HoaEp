package com.dearfloral.module.users.dto;

public record ProfileResponse(
        Long userId,
        String fullName,
        String phone,
        String email,
        String role,
        String status
) {
}
