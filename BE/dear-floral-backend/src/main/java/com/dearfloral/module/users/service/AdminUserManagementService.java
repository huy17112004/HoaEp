package com.dearfloral.module.users.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.RoleEntity;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.RoleRepository;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.availableorders.repository.AvailableOrderRepository;
import com.dearfloral.module.customorders.repository.CustomOrderRepository;
import com.dearfloral.module.reports.service.AuditLogService;
import com.dearfloral.module.users.dto.AdminUserResponse;
import com.dearfloral.module.users.dto.CreateAdminUserRequest;
import com.dearfloral.module.users.dto.UpdateAdminUserRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserManagementService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_LOCKED = "LOCKED";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AvailableOrderRepository availableOrderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final AuditLogService auditLogService;

    public AdminUserManagementService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AvailableOrderRepository availableOrderRepository,
            CustomOrderRepository customOrderRepository,
            AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.availableOrderRepository = availableOrderRepository;
        this.customOrderRepository = customOrderRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> getUsers(
            String keyword,
            RoleCode roleCode,
            String status,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<UserEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("fullName")), like),
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("phone")), like)
                ));
            }
            if (roleCode != null) {
                predicates.add(cb.equal(root.get("role").get("code"), roleCode));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("status")), status.trim().toUpperCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional
    public AdminUserResponse createUser(CreateAdminUserRequest request, Long actorUserId) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists.");
        }

        RoleEntity role = roleRepository.findByCode(request.role())
                .orElseThrow(() -> new NotFoundException("ROLE_NOT_FOUND", "Role not found."));

        UserEntity user = new UserEntity();
        user.setFullName(request.fullName().trim());
        user.setPhone(request.phone().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);
        user.setStatus(resolveStatus(request.status()));
        UserEntity saved = userRepository.save(user);

        auditLogService.logAction(actorUserId, "ADMIN_USER_CREATED", "USER", saved.getId(), "role=" + role.getCode());
        return toResponse(saved);
    }

    @Transactional
    public AdminUserResponse updateUser(Long userId, UpdateAdminUserRequest request, Long actorUserId) {
        UserEntity user = userRepository.findWithRoleById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));

        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmailAndIdNot(normalizedEmail, userId)) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists.");
        }

        RoleEntity role = roleRepository.findByCode(request.role())
                .orElseThrow(() -> new NotFoundException("ROLE_NOT_FOUND", "Role not found."));

        user.setFullName(request.fullName().trim());
        user.setPhone(request.phone().trim());
        user.setEmail(normalizedEmail);
        user.setRole(role);
        user.setStatus(resolveStatus(request.status()));
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        UserEntity saved = userRepository.save(user);
        auditLogService.logAction(actorUserId, "ADMIN_USER_UPDATED", "USER", saved.getId(), "role=" + role.getCode());
        return toResponse(saved);
    }

    @Transactional
    public void deleteOrLockUser(Long userId, Long actorUserId) {
        if (userId.equals(actorUserId)) {
            throw new BusinessException("SELF_ACTION_NOT_ALLOWED", "Cannot delete or lock your own account.");
        }

        UserEntity user = userRepository.findWithRoleById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));

        boolean hasOrderHistory = availableOrderRepository.existsByCustomerUserId(userId)
                || customOrderRepository.existsByCustomerUserId(userId);
        if (hasOrderHistory) {
            user.setStatus(STATUS_LOCKED);
            userRepository.save(user);
            auditLogService.logAction(actorUserId, "ADMIN_USER_LOCKED", "USER", userId, "lockedBecause=orderHistory");
            return;
        }

        userRepository.delete(user);
        auditLogService.logAction(actorUserId, "ADMIN_USER_DELETED", "USER", userId, "deleted=true");
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private AdminUserResponse toResponse(UserEntity user) {
        return new AdminUserResponse(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole().getCode().name(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private String resolveStatus(String status) {
        if (status == null || status.isBlank()) {
            return STATUS_ACTIVE;
        }
        return status.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
