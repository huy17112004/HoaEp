package com.dearfloral.module.categories.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.products.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_categories")
public class ProductCategoryEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> products = new ArrayList<>();
}
