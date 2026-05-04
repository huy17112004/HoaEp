package com.dearfloral.config.bootstrap;

import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.module.auth.entity.RoleEntity;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.RoleRepository;
import com.dearfloral.module.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
public class AdminAccountInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD_HASH = "$2a$10$XPAkd91DNY5MPx84l.WTHu51jr0tISSlhxj0r2DkixL8QQwLHE2IO";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminAccountInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            log.info("Admin seed skipped: account {} already exists.", ADMIN_EMAIL);
            return;
        }

        RoleEntity adminRole = roleRepository.findByCode(RoleCode.ADMIN)
                .orElseThrow(() -> new IllegalStateException("ADMIN role is not configured."));

        UserEntity admin = new UserEntity();
        admin.setFullName("System Admin");
        admin.setPhone("0900000000");
        admin.setEmail(ADMIN_EMAIL);
        admin.setPasswordHash(ADMIN_PASSWORD_HASH);
        admin.setRole(adminRole);
        admin.setStatus("ACTIVE");
        userRepository.save(admin);

        log.info("Seeded default admin account: {}", ADMIN_EMAIL);
    }
}
