package com.dearfloral.module.reports.repository;

import com.dearfloral.module.reports.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
}
