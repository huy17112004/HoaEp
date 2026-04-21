package com.dearfloral.module.inventory.repository;

import com.dearfloral.module.inventory.entity.InventoryItemEntity;
import com.dearfloral.common.enums.ProductKind;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long>, JpaSpecificationExecutor<InventoryItemEntity> {
    Optional<InventoryItemEntity> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<InventoryItemEntity> findWithLockByProductId(Long productId);

    @Query("""
            select i.product.id as productId,
                   i.product.name as productName,
                   i.product.productKind as productKind,
                   i.quantityOnHand as quantityOnHand
            from InventoryItemEntity i
            where i.quantityOnHand <= :threshold
            order by i.quantityOnHand asc, i.product.name asc
            """)
    List<LowInventoryProjection> findLowInventoryProducts(@Param("threshold") Integer threshold);

    @Query("""
            select i.product.id as productId,
                   i.product.name as productName,
                   i.product.productKind as productKind,
                   i.quantityOnHand as quantityOnHand,
                   i.updatedAt as updatedAt
            from InventoryItemEntity i
            where (:productKind is null or i.product.productKind = :productKind)
            order by i.quantityOnHand asc, i.product.name asc
            """)
    List<InventoryReportProjection> summarizeInventory(@Param("productKind") ProductKind productKind);

    interface LowInventoryProjection {
        Long getProductId();
        String getProductName();
        ProductKind getProductKind();
        Integer getQuantityOnHand();
    }

    interface InventoryReportProjection {
        Long getProductId();
        String getProductName();
        ProductKind getProductKind();
        Integer getQuantityOnHand();
        java.time.LocalDateTime getUpdatedAt();
    }
}
