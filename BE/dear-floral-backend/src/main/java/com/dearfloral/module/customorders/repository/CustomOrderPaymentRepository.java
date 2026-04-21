package com.dearfloral.module.customorders.repository;

import com.dearfloral.common.enums.CustomPaymentStage;
import com.dearfloral.module.customorders.entity.CustomOrderPaymentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomOrderPaymentRepository extends JpaRepository<CustomOrderPaymentEntity, Long> {
    List<CustomOrderPaymentEntity> findByCustomOrderIdOrderByCreatedAtDesc(Long customOrderId);
    boolean existsByCustomOrderIdAndPaymentStage(Long customOrderId, CustomPaymentStage paymentStage);
    Optional<CustomOrderPaymentEntity> findByCustomOrderIdAndPaymentStage(Long customOrderId, CustomPaymentStage paymentStage);

    @Query(value = """
            select cast(date_trunc(:groupBy, p.paid_at) as date) as bucketDate,
                   sum(p.amount) as revenue
            from custom_order_payments p
            where p.payment_status = 'PAID'
              and (:fromDate is null or p.paid_at >= :fromDate)
              and (:toDate is null or p.paid_at <= :toDate)
            group by cast(date_trunc(:groupBy, p.paid_at) as date)
            order by bucketDate
            """, nativeQuery = true)
    List<RevenueBucketProjection> summarizeRevenueByBucket(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate,
            @Param("groupBy") String groupBy
    );

    interface RevenueBucketProjection {
        java.time.LocalDate getBucketDate();
        java.math.BigDecimal getRevenue();
    }
}
