package com.dearfloral.module.customorders.repository;

import com.dearfloral.module.customorders.entity.CustomOrderEntity;
import com.dearfloral.common.enums.CustomOrderStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomOrderRepository extends JpaRepository<CustomOrderEntity, Long>, JpaSpecificationExecutor<CustomOrderEntity> {
    Optional<CustomOrderEntity> findByOrderCode(String orderCode);
    Optional<CustomOrderEntity> findByIdAndCustomerUserId(Long id, Long customerUserId);
    boolean existsByCustomerUserId(Long customerUserId);
    long countByOrderStatus(CustomOrderStatus orderStatus);

    @Query("""
            select count(c)
            from CustomOrderEntity c
            where (:fromDate is null or c.orderedAt >= :fromDate)
              and (:toDate is null or c.orderedAt <= :toDate)
            """)
    long countInRange(@Param("fromDate") java.time.LocalDateTime fromDate, @Param("toDate") java.time.LocalDateTime toDate);

    @Query("""
            select count(c)
            from CustomOrderEntity c
            where c.orderStatus in :statuses
              and (:fromDate is null or c.orderedAt >= :fromDate)
              and (:toDate is null or c.orderedAt <= :toDate)
            """)
    long countByStatusInRange(
            @Param("statuses") java.util.Collection<CustomOrderStatus> statuses,
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    @Query("""
            select c.id as orderId,
                   c.orderCode as orderCode,
                   c.orderedAt as orderedAt,
                   c.orderStatus as orderStatus,
                   c.totalAmount as totalAmount
            from CustomOrderEntity c
            where c.orderedAt >= :fromDate and c.orderedAt <= :toDate
            order by c.orderedAt desc
            """)
    java.util.List<RecentCustomOrderProjection> findRecentOrdersInRange(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    @Query("""
            select c.orderStatus as orderStatus,
                   count(c) as totalOrders
            from CustomOrderEntity c
            where (:fromDate is null or c.orderedAt >= :fromDate)
              and (:toDate is null or c.orderedAt <= :toDate)
            group by c.orderStatus
            order by c.orderStatus
            """)
    java.util.List<CustomOrderStatusCountProjection> summarizeOrderStatusInRange(
            @Param("fromDate") java.time.LocalDateTime fromDate,
            @Param("toDate") java.time.LocalDateTime toDate
    );

    interface RecentCustomOrderProjection {
        Long getOrderId();
        String getOrderCode();
        java.time.LocalDateTime getOrderedAt();
        CustomOrderStatus getOrderStatus();
        java.math.BigDecimal getTotalAmount();
    }

    interface CustomOrderStatusCountProjection {
        CustomOrderStatus getOrderStatus();
        Long getTotalOrders();
    }
}
