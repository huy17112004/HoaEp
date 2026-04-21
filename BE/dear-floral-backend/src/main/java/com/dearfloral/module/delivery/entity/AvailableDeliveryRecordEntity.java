package com.dearfloral.module.delivery.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.availableorders.entity.AvailableOrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "available_delivery_records")
public class AvailableDeliveryRecordEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "available_order_id", nullable = false)
    private AvailableOrderEntity availableOrder;

    @Column(name = "delivery_status", nullable = false, length = 50)
    private String deliveryStatus;

    @Column(name = "shipped_time")
    private LocalDateTime shippedTime;

    @Column(name = "delivered_time")
    private LocalDateTime deliveredTime;

    @Column(name = "receiver_note", length = 500)
    private String receiverNote;
}
