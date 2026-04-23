package com.dearfloral.module.users.dto;

import java.time.LocalDateTime;

public record AdminUserResponse(
        Long userId,
        String fullName,
        String phone,
        String email,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
