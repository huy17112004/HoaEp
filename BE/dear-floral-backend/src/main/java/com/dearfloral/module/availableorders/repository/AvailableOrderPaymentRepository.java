package com.dearfloral.module.availableorders.repository;

import com.dearfloral.module.availableorders.entity.AvailableOrderPaymentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvailableOrderPaymentRepository extends JpaRepository<AvailableOrderPaymentEntity, Long> {
    List<AvailableOrderPaymentEntity> findByAvailableOrderIdOrderByCreatedAtDesc(Long availableOrderId);

    @Query(value = """
            select cast(date_trunc('day', p.paid_at) as date) as bucketDate,
                   sum(p.amount) as revenue
            from available_order_payments p
            where p.payment_status = 'PAID'
              and p.paid_at >= :fromDate
              and p.paid_at <= :toDate
            group by cast(date_trunc('day', p.paid_at) as date)
            order by bucketDate
            """, nativeQuery = true)
    List<RevenueBucketProjection> summarizeRevenueByDay(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    @Query(value = """
            select cast(date_trunc('month', p.paid_at) as date) as bucketDate,
                   sum(p.amount) as revenue
            from available_order_payments p
            where p.payment_status = 'PAID'
              and p.paid_at >= :fromDate
              and p.paid_at <= :toDate
            group by cast(date_trunc('month', p.paid_at) as date)
            order by bucketDate
            """, nativeQuery = true)
    List<RevenueBucketProjection> summarizeRevenueByMonth(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    interface RevenueBucketProjection {
        java.time.LocalDate getBucketDate();
        java.math.BigDecimal getRevenue();
    }
}
