package com.dearfloral.module.purchasereceipts.repository;

import com.dearfloral.module.purchasereceipts.entity.PurchaseReceiptItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseReceiptItemRepository extends JpaRepository<PurchaseReceiptItemEntity, Long> {
    List<PurchaseReceiptItemEntity> findByPurchaseReceiptId(Long purchaseReceiptId);
}
