package com.dearfloral.module.purchasereceipts.entity;

import com.dearfloral.common.entity.BaseEntity;
import com.dearfloral.module.auth.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase_receipts")
public class PurchaseReceiptEntity extends BaseEntity {

    @Column(name = "receipt_code", nullable = false, unique = true, length = 50)
    private String receiptCode;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @Column(name = "note", length = 500)
    private String note;

    @OneToMany(mappedBy = "purchaseReceipt")
    private List<PurchaseReceiptItemEntity> items = new ArrayList<>();
}
