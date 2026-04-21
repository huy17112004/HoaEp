package com.dearfloral.module.users.repository;

import com.dearfloral.module.users.entity.CustomerProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfileEntity, Long> {
    Optional<CustomerProfileEntity> findByUserId(Long userId);
}
