package com.dearfloral.module.availableorders.repository;

import com.dearfloral.module.availableorders.entity.AvailableOrderItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableOrderItemRepository extends JpaRepository<AvailableOrderItemEntity, Long> {
    List<AvailableOrderItemEntity> findByAvailableOrderId(Long availableOrderId);
}
