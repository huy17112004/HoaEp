package com.dearfloral.module.reports.service;

import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.reports.entity.AuditLogEntity;
import com.dearfloral.module.reports.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void logAction(Long actorUserId, String action, String targetType, Long targetId, String payloadSummary) {
        AuditLogEntity log = new AuditLogEntity();
        if (actorUserId != null) {
            UserEntity actor = userRepository.findById(actorUserId).orElse(null);
            log.setActorUser(actor);
        }
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setPayloadSummary(payloadSummary);
        auditLogRepository.save(log);
    }
}
