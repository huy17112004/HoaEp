package com.dearfloral.module.products.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.categories.entity.ProductCategoryEntity;
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
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategoryEntity category;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_kind", nullable = false, length = 50)
    private ProductKind productKind;

    @Column(name = "is_sellable_directly", nullable = false)
    private Boolean isSellableDirectly;

    @Column(name = "is_custom_selectable", nullable = false)
    private Boolean isCustomSelectable;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "size", length = 100)
    private String size;

    @Column(name = "material", length = 100)
    private String material;

    @Column(name = "flower_type", length = 100)
    private String flowerType;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @OneToMany(mappedBy = "product")
    private List<ProductImageEntity> productImages = new ArrayList<>();
}
