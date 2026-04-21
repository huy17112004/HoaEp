package com.dearfloral.config.security;

import com.dearfloral.common.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {
    private CurrentUser() {
    }

    public static Long userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthException("Unauthorized.");
        }
        return principal.getUserId();
    }
}
