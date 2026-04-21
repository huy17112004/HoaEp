package com.dearfloral.module.delivery.repository;

import com.dearfloral.module.delivery.entity.CustomDeliveryRecordEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomDeliveryRecordRepository extends JpaRepository<CustomDeliveryRecordEntity, Long> {
    List<CustomDeliveryRecordEntity> findByCustomOrderIdOrderByCreatedAtDesc(Long customOrderId);
    Optional<CustomDeliveryRecordEntity> findTopByCustomOrderIdOrderByCreatedAtDesc(Long customOrderId);
}
