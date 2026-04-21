package com.dearfloral.module.customorders.repository;

import com.dearfloral.common.enums.DemoResponseStatus;
import com.dearfloral.module.customorders.entity.CustomDemoEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomDemoRepository extends JpaRepository<CustomDemoEntity, Long> {
    List<CustomDemoEntity> findByCustomOrderIdOrderByVersionNoDesc(Long customOrderId);
    List<CustomDemoEntity> findByCustomOrderIdOrderByVersionNoAsc(Long customOrderId);
    Optional<CustomDemoEntity> findTopByCustomOrderIdOrderByVersionNoDesc(Long customOrderId);
    Optional<CustomDemoEntity> findByIdAndCustomOrderId(Long id, Long customOrderId);
    long countByCustomerResponseStatus(DemoResponseStatus customerResponseStatus);
}
