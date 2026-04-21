package com.dearfloral.module.users.entity;

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
@Table(name = "customer_addresses")
public class CustomerAddressEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_user_id", nullable = false)
    private UserEntity customerUser;

    @Column(name = "receiver_name", nullable = false, length = 150)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @Column(name = "ward", nullable = false, length = 100)
    private String ward;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "note", length = 255)
    private String note;
}
