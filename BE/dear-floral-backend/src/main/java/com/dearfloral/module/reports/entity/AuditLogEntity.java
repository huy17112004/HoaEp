package com.dearfloral.module.reports.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.auth.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
public class AuditLogEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private UserEntity actorUser;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "target_type", nullable = false, length = 100)
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "payload_summary", length = 2000)
    private String payloadSummary;
}
