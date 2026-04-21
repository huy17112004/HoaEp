package com.dearfloral.module.availableorders.repository;

import com.dearfloral.module.availableorders.entity.AvailableOrderEntity;
import com.dearfloral.common.enums.AvailableOrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvailableOrderRepository extends JpaRepository<AvailableOrderEntity, Long> {
    Optional<AvailableOrderEntity> findByOrderCode(String orderCode);
    Optional<AvailableOrderEntity> findByIdAndCustomerUserId(Long id, Long customerUserId);
    List<AvailableOrderEntity> findByCustomerUserIdOrderByOrderedAtDesc(Long customerUserId);
    long countByOrderStatus(AvailableOrderStatus orderStatus);

    @Query("""
            select count(a)
            from AvailableOrderEntity a
            where (:fromDate is null or a.orderedAt >= :fromDate)
              and (:toDate is null or a.orderedAt <= :toDate)
            """)
    long countInRange(@Param("fromDate") java.time.LocalDateTime fromDate, @Param("toDate") java.time.LocalDateTime toDate);

    @Query("""
            select count(a)
            from AvailableOrderEntity a
            where a.orderStatus in :statuses
              and (:fromDate is null or a.orderedAt >= :fromDate)
              and (:toDate is null or a.orderedAt <= :toDate)
            """)
    long countByStatusInRange(
            @Param("statuses") java.util.Collection<AvailableOrderStatus> statuses,
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    @Query("""
            select a.id as orderId,
                   a.orderCode as orderCode,
                   a.orderedAt as orderedAt,
                   a.orderStatus as orderStatus,
                   a.totalAmount as totalAmount
            from AvailableOrderEntity a
            where a.orderedAt >= :fromDate and a.orderedAt <= :toDate
            order by a.orderedAt desc
            """)
    List<RecentAvailableOrderProjection> findRecentOrdersInRange(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    @Query("""
            select a.orderStatus as orderStatus,
                   count(a) as totalOrders
            from AvailableOrderEntity a
            where (:fromDate is null or a.orderedAt >= :fromDate)
              and (:toDate is null or a.orderedAt <= :toDate)
            group by a.orderStatus
            order by a.orderStatus
            """)
    List<AvailableOrderStatusCountProjection> summarizeOrderStatusInRange(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    interface RecentAvailableOrderProjection {
        Long getOrderId();
        String getOrderCode();
        java.time.LocalDateTime getOrderedAt();
        AvailableOrderStatus getOrderStatus();
        java.math.BigDecimal getTotalAmount();
    }

    interface AvailableOrderStatusCountProjection {
        AvailableOrderStatus getOrderStatus();
        Long getTotalOrders();
    }
}
