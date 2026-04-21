package com.dearfloral.module.delivery.repository;

import com.dearfloral.module.delivery.entity.AvailableDeliveryRecordEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableDeliveryRecordRepository extends JpaRepository<AvailableDeliveryRecordEntity, Long> {
    List<AvailableDeliveryRecordEntity> findByAvailableOrderIdOrderByCreatedAtDesc(Long availableOrderId);
    Optional<AvailableDeliveryRecordEntity> findTopByAvailableOrderIdOrderByCreatedAtDesc(Long availableOrderId);
}
