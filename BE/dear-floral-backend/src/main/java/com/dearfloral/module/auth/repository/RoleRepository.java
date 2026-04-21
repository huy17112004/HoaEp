package com.dearfloral.module.auth.repository;

import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.module.auth.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByCode(RoleCode code);
}
