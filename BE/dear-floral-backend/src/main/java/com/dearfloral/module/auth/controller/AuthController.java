package com.dearfloral.module.auth.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.module.auth.dto.LoginRequest;
import com.dearfloral.module.auth.dto.LoginResponse;
import com.dearfloral.module.auth.dto.RegisterRequest;
import com.dearfloral.module.auth.dto.RegisterResponse;
import com.dearfloral.module.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse data = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("AUTH_REGISTER_SUCCESS", "Register successfully.", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse data = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("AUTH_LOGIN_SUCCESS", "Login successfully.", data));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success("AUTH_LOGOUT_SUCCESS", "Logout successfully.", null));
    }
}
