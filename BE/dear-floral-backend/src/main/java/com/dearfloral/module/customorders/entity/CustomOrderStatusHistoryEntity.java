package com.dearfloral.module.customorders.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.module.auth.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "custom_order_status_histories")
public class CustomOrderStatusHistoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_order_id", nullable = false)
    private CustomOrderEntity customOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false, length = 50)
    private CustomOrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 50)
    private CustomOrderStatus toStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by", nullable = false)
    private UserEntity changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "reason", length = 500)
    private String reason;
}
