package com.dearfloral.module.users.service;

import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.users.dto.AddressResponse;
import com.dearfloral.module.users.dto.ProfileResponse;
import com.dearfloral.module.users.dto.UpdateProfileRequest;
import com.dearfloral.module.users.dto.UpsertAddressRequest;
import com.dearfloral.module.users.entity.CustomerAddressEntity;
import com.dearfloral.module.users.repository.CustomerAddressRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public UserService(
            UserRepository userRepository,
            CustomerAddressRepository customerAddressRepository
    ) {
        this.userRepository = userRepository;
        this.customerAddressRepository = customerAddressRepository;
    }

    public ProfileResponse getMyProfile(Long userId) {
        UserEntity user = getUserOrThrow(userId);
        return toProfileResponse(user);
    }

    @Transactional
    public ProfileResponse updateMyProfile(Long userId, UpdateProfileRequest request) {
        UserEntity user = getUserOrThrow(userId);
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmailAndIdNot(normalizedEmail, userId)) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists.");
        }
        user.setFullName(request.fullName().trim());
        user.setPhone(request.phone().trim());
        user.setEmail(normalizedEmail);
        UserEntity saved = userRepository.save(user);
        return toProfileResponse(saved);
    }

    public List<AddressResponse> getMyAddresses(Long userId) {
        getUserOrThrow(userId);
        return customerAddressRepository.findByCustomerUserId(userId).stream()
                .map(this::toAddressResponse)
                .toList();
    }

    @Transactional
    public AddressResponse createAddress(Long userId, UpsertAddressRequest request) {
        UserEntity user = getUserOrThrow(userId);
        if (Boolean.TRUE.equals(request.isDefault())) {
            customerAddressRepository.resetDefaultByCustomerUserId(userId);
        }

        CustomerAddressEntity address = new CustomerAddressEntity();
        address.setCustomerUser(user);
        applyAddressRequest(address, request);
        CustomerAddressEntity saved = customerAddressRepository.save(address);
        return toAddressResponse(saved);
    }

    @Transactional
    public AddressResponse updateAddress(Long userId, Long addressId, UpsertAddressRequest request) {
        getUserOrThrow(userId);
        CustomerAddressEntity address = customerAddressRepository.findByIdAndCustomerUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("ADDRESS_NOT_FOUND", "Address not found."));

        if (Boolean.TRUE.equals(request.isDefault())) {
            customerAddressRepository.resetDefaultByCustomerUserId(userId);
        }
        applyAddressRequest(address, request);
        CustomerAddressEntity saved = customerAddressRepository.save(address);
        return toAddressResponse(saved);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        getUserOrThrow(userId);
        CustomerAddressEntity address = customerAddressRepository.findByIdAndCustomerUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("ADDRESS_NOT_FOUND", "Address not found."));
        customerAddressRepository.delete(address);
    }

    private void applyAddressRequest(CustomerAddressEntity address, UpsertAddressRequest request) {
        address.setReceiverName(request.receiverName().trim());
        address.setReceiverPhone(request.receiverPhone().trim());
        address.setAddressLine(request.addressLine().trim());
        address.setWard(request.ward().trim());
        address.setDistrict(request.district().trim());
        address.setProvince(request.province().trim());
        address.setIsDefault(request.isDefault());
        address.setNote(request.note() == null ? null : request.note().trim());
    }

    private ProfileResponse toProfileResponse(UserEntity user) {
        return new ProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole().getCode().name(),
                user.getStatus()
        );
    }

    private AddressResponse toAddressResponse(CustomerAddressEntity address) {
        return new AddressResponse(
                address.getId(),
                address.getReceiverName(),
                address.getReceiverPhone(),
                address.getAddressLine(),
                address.getWard(),
                address.getDistrict(),
                address.getProvince(),
                address.getIsDefault(),
                address.getNote()
        );
    }

    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
