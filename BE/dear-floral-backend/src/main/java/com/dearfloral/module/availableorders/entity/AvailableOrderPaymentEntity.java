package com.dearfloral.module.availableorders.entity;

import com.dearfloral.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "available_order_payments")
public class AvailableOrderPaymentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "available_order_id", nullable = false)
    private AvailableOrderEntity availableOrder;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(name = "transaction_ref", length = 100)
    private String transactionRef;

    @Column(name = "payment_proof_url", length = 500)
    private String paymentProofUrl;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "note", length = 500)
    private String note;
}
