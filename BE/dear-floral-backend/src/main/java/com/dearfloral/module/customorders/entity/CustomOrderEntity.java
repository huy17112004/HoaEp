package com.dearfloral.module.customorders.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.FlowerEvaluationStatus;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.products.entity.ProductEntity;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "custom_orders")
public class CustomOrderEntity extends BaseEntity {

    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_user_id", nullable = false)
    private UserEntity customerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private CustomerAddressEntity shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selected_frame_product_id", nullable = false)
    private ProductEntity selectedFrameProduct;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private CustomOrderStatus orderStatus;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(name = "deposit_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "remaining_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "flower_type", nullable = false, length = 100)
    private String flowerType;

    @Column(name = "personalization_content", length = 2000)
    private String personalizationContent;

    @Column(name = "requested_delivery_date")
    private LocalDate requestedDeliveryDate;

    @Column(name = "flower_input_image_url", nullable = false, length = 500)
    private String flowerInputImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "flower_evaluation_status", nullable = false, length = 50)
    private FlowerEvaluationStatus flowerEvaluationStatus;

    @Column(name = "flower_evaluation_note", length = 500)
    private String flowerEvaluationNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private UserEntity assignedStaff;

    @Column(name = "demo_revision_count", nullable = false)
    private Integer demoRevisionCount;

    @Column(name = "extra_revision_fee_rate", precision = 8, scale = 4)
    private BigDecimal extraRevisionFeeRate;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "note", length = 500)
    private String note;

    @OneToMany(mappedBy = "customOrder")
    private List<CustomDemoEntity> demos = new ArrayList<>();

    @OneToMany(mappedBy = "customOrder")
    private List<CustomOrderStatusHistoryEntity> statusHistories = new ArrayList<>();

    @OneToMany(mappedBy = "customOrder")
    private List<CustomOrderPaymentEntity> payments = new ArrayList<>();
}
