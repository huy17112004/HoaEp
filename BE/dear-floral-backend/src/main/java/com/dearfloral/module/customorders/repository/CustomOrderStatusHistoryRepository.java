package com.dearfloral.module.customorders.repository;

import com.dearfloral.module.customorders.entity.CustomOrderStatusHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomOrderStatusHistoryRepository extends JpaRepository<CustomOrderStatusHistoryEntity, Long> {
    List<CustomOrderStatusHistoryEntity> findByCustomOrderIdOrderByChangedAtDesc(Long customOrderId);
}
