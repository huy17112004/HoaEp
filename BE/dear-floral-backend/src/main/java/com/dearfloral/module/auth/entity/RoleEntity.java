package com.dearfloral.module.auth.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.RoleCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private RoleCode code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "role")
    private List<UserEntity> users = new ArrayList<>();
}
