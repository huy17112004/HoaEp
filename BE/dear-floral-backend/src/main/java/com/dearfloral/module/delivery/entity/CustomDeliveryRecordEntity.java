package com.dearfloral.module.delivery.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.DeliveryType;
import com.dearfloral.module.customorders.entity.CustomOrderEntity;
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
@Table(name = "custom_delivery_records")
public class CustomDeliveryRecordEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_order_id", nullable = false)
    private CustomOrderEntity customOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false, length = 50)
    private DeliveryType deliveryType;

    @Column(name = "delivery_status", nullable = false, length = 50)
    private String deliveryStatus;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "shipped_time")
    private LocalDateTime shippedTime;

    @Column(name = "delivered_time")
    private LocalDateTime deliveredTime;

    @Column(name = "receiver_note", length = 500)
    private String receiverNote;
}
