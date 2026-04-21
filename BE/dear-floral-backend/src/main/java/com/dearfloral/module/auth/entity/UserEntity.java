package com.dearfloral.module.auth.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.users.entity.CustomerAddressEntity;
import com.dearfloral.module.users.entity.CustomerProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private CustomerProfileEntity customerProfile;

    @OneToMany(mappedBy = "customerUser", fetch = FetchType.LAZY)
    private List<CustomerAddressEntity> customerAddresses = new ArrayList<>();
}
