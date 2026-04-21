package com.dearfloral.module.inventory.repository;

import com.dearfloral.module.inventory.entity.InventoryTransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransactionEntity, Long> {
    List<InventoryTransactionEntity> findByProductIdOrderByCreatedAtDesc(Long productId);
}
