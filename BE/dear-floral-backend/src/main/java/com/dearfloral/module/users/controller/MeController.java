package com.dearfloral.module.users.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.users.dto.AddressResponse;
import com.dearfloral.module.users.dto.ProfileResponse;
import com.dearfloral.module.users.dto.UpdateProfileRequest;
import com.dearfloral.module.users.dto.UpsertAddressRequest;
import com.dearfloral.module.users.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final UserService userService;

    public MeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ProfileResponse data = userService.getMyProfile(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("USER_PROFILE_FETCHED", "Profile fetched successfully.", data));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        ProfileResponse data = userService.updateMyProfile(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("USER_PROFILE_UPDATED", "Profile updated successfully.", data));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<AddressResponse> data = userService.getMyAddresses(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("ADDRESS_LIST_FETCHED", "Address list fetched successfully.", data));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpsertAddressRequest request
    ) {
        AddressResponse data = userService.createAddress(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("ADDRESS_CREATED", "Address created successfully.", data));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long addressId,
            @Valid @RequestBody UpsertAddressRequest request
    ) {
        AddressResponse data = userService.updateAddress(principal.getUserId(), addressId, request);
        return ResponseEntity.ok(ApiResponse.success("ADDRESS_UPDATED", "Address updated successfully.", data));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long addressId
    ) {
        userService.deleteAddress(principal.getUserId(), addressId);
        return ResponseEntity.ok(ApiResponse.success("ADDRESS_DELETED", "Address deleted successfully.", null));
    }
}
