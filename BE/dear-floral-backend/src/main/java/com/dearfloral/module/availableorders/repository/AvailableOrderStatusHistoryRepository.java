package com.dearfloral.module.availableorders.repository;

import com.dearfloral.module.availableorders.entity.AvailableOrderStatusHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableOrderStatusHistoryRepository extends JpaRepository<AvailableOrderStatusHistoryEntity, Long> {
    List<AvailableOrderStatusHistoryEntity> findByAvailableOrderIdOrderByChangedAtDesc(Long availableOrderId);
}
