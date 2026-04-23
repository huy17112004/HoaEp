package com.dearfloral.module.users.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.users.dto.AdminUserResponse;
import com.dearfloral.module.users.dto.CreateAdminUserRequest;
import com.dearfloral.module.users.dto.UpdateAdminUserRequest;
import com.dearfloral.module.users.service.AdminUserManagementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserManagementService adminUserManagementService;

    public AdminUserController(AdminUserManagementService adminUserManagementService) {
        this.adminUserManagementService = adminUserManagementService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<AdminUserResponse>>> getUsers(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RoleCode role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        ensureAdmin(principal);
        Page<AdminUserResponse> data = adminUserManagementService.getUsers(keyword, role, status, page, limit);
        PageMeta meta = adminUserManagementService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("ADMIN_USER_LIST_FETCHED", "User list fetched successfully.", data.getContent(), meta));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminUserResponse>> createUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateAdminUserRequest request
    ) {
        ensureAdmin(principal);
        AdminUserResponse data = adminUserManagementService.createUser(request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("ADMIN_USER_CREATED", "User created successfully.", data));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateAdminUserRequest request
    ) {
        ensureAdmin(principal);
        AdminUserResponse data = adminUserManagementService.updateUser(userId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("ADMIN_USER_UPDATED", "User updated successfully.", data));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long userId
    ) {
        ensureAdmin(principal);
        adminUserManagementService.deleteOrLockUser(userId, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("ADMIN_USER_DELETED_OR_LOCKED", "User deleted or locked successfully.", null));
    }

    private void ensureAdmin(UserPrincipal principal) {
        if (!RoleCode.ADMIN.name().equals(principal.getRole())) {
            throw new AccessDeniedException("Admin role is required.");
        }
    }
}
