package com.dearfloral.module.availableorders.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.users.entity.CustomerAddressEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "available_orders")
public class AvailableOrderEntity extends BaseEntity {

    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_user_id", nullable = false)
    private UserEntity customerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private CustomerAddressEntity shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private AvailableOrderStatus orderStatus;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private UserEntity assignedStaff;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "refund_bank_name", length = 150)
    private String refundBankName;

    @Column(name = "refund_account_number", length = 50)
    private String refundAccountNumber;

    @Column(name = "refund_account_name", length = 150)
    private String refundAccountName;

    @Column(name = "refund_requested_at")
    private LocalDateTime refundRequestedAt;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @OneToMany(mappedBy = "availableOrder")
    private List<AvailableOrderItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "availableOrder")
    private List<AvailableOrderStatusHistoryEntity> statusHistories = new ArrayList<>();

    @OneToMany(mappedBy = "availableOrder")
    private List<AvailableOrderPaymentEntity> payments = new ArrayList<>();
}
