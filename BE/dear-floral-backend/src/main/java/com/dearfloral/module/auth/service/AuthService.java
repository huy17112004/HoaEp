package com.dearfloral.module.auth.service;

import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.common.exception.AuthException;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.ErrorCode;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.config.security.CustomUserDetailsService;
import com.dearfloral.config.security.JwtService;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.auth.dto.LoginRequest;
import com.dearfloral.module.auth.dto.LoginResponse;
import com.dearfloral.module.auth.dto.RegisterRequest;
import com.dearfloral.module.auth.dto.RegisterResponse;
import com.dearfloral.module.auth.dto.UserProfileResponse;
import com.dearfloral.module.auth.entity.RoleEntity;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.RoleRepository;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.users.entity.CustomerProfileEntity;
import com.dearfloral.module.users.repository.CustomerProfileRepository;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CustomerProfileRepository customerProfileRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists.");
        }

        RoleEntity customerRole = roleRepository.findByCode(RoleCode.CUSTOMER)
                .orElseThrow(() -> new NotFoundException("ROLE_NOT_FOUND", "Customer role is not configured."));

        UserEntity user = new UserEntity();
        user.setFullName(request.fullName().trim());
        user.setPhone(request.phone().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(customerRole);
        user.setStatus("ACTIVE");
        UserEntity savedUser = userRepository.save(user);

        CustomerProfileEntity profile = new CustomerProfileEntity();
        profile.setUser(savedUser);
        profile.setNote(null);
        customerProfileRepository.save(profile);

        return new RegisterResponse(savedUser.getId(), savedUser.getRole().getCode().name());
    }

    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
            );
        } catch (AuthenticationException ex) {
            throw new AuthException(ErrorCode.UNAUTHORIZED, "Invalid email or password.");
        }

        UserEntity user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new AuthException("Invalid email or password."));
        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new AuthException("Account is inactive.");
        }

        UserPrincipal principal = customUserDetailsService.toPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);
        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole().getCode().name(),
                user.getStatus()
        );
        return new LoginResponse(accessToken, profile, user.getRole().getCode().name());
    }

    public void logout() {
        // Stateless JWT logout handled on client by discarding token.
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
