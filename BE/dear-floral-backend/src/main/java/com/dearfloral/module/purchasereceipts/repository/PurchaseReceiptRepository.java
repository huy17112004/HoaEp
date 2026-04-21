package com.dearfloral.module.purchasereceipts.repository;

import com.dearfloral.module.purchasereceipts.entity.PurchaseReceiptEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceiptEntity, Long>, JpaSpecificationExecutor<PurchaseReceiptEntity> {
    Optional<PurchaseReceiptEntity> findByReceiptCode(String receiptCode);
}
