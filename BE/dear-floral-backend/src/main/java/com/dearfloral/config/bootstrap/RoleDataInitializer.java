package com.dearfloral.config.bootstrap;

import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.module.auth.entity.RoleEntity;
import com.dearfloral.module.auth.repository.RoleRepository;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)
public class RoleDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleDataInitializer.class);

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Map<RoleCode, String> roleNames = new EnumMap<>(RoleCode.class);
        roleNames.put(RoleCode.ADMIN, "Admin");
        roleNames.put(RoleCode.STAFF, "Staff");
        roleNames.put(RoleCode.CUSTOMER, "Customer");

        Map<RoleCode, String> roleDescriptions = new EnumMap<>(RoleCode.class);
        roleDescriptions.put(RoleCode.ADMIN, "System administrator");
        roleDescriptions.put(RoleCode.STAFF, "Internal staff");
        roleDescriptions.put(RoleCode.CUSTOMER, "Customer account");

        int createdCount = 0;
        for (RoleCode code : RoleCode.values()) {
            if (roleRepository.findByCode(code).isPresent()) {
                continue;
            }

            RoleEntity role = new RoleEntity();
            role.setCode(code);
            role.setName(roleNames.get(code));
            role.setDescription(roleDescriptions.get(code));
            roleRepository.save(role);
            createdCount++;
        }

        if (createdCount > 0) {
            log.info("Seeded {} role(s) from RoleCode enum.", createdCount);
        } else {
            log.info("Role seed skipped: all RoleCode values already exist.");
        }
    }
}

