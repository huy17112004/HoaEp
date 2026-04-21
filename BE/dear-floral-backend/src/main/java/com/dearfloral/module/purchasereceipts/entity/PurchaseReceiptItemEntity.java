package com.dearfloral.module.purchasereceipts.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.products.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase_receipt_items")
public class PurchaseReceiptItemEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_receipt_id", nullable = false)
    private PurchaseReceiptEntity purchaseReceipt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;
}
