package com.dearfloral.module.auth.dto;

public record LoginResponse(
        String accessToken,
        UserProfileResponse userProfile,
        String role
) {
}
