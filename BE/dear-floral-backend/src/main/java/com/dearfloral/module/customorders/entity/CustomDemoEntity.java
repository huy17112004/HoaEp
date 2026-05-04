package com.dearfloral.module.customorders.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.DemoResponseStatus;
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
@Table(name = "custom_demos")
public class CustomDemoEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_order_id", nullable = false)
    private CustomOrderEntity customOrder;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Column(name = "demo_image_url", nullable = false, length = 500)
    private String demoImageUrl;

    @Column(name = "demo_description", length = 1000)
    private String demoDescription;

    @Column(name = "demo_image_urls", length = 4000)
    private String demoImageUrls;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_response_status", nullable = false, length = 50)
    private DemoResponseStatus customerResponseStatus;

    @Column(name = "customer_feedback", length = 1000)
    private String customerFeedback;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private UserEntity uploadedBy;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
}
