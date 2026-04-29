package com.dearfloral.module.products.repository;

import com.dearfloral.module.products.entity.ProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
    boolean existsByCategoryId(Long categoryId);

    @Query("""
            select count(p)
            from ProductEntity p
            where p.createdAt >= :fromDate
              and p.createdAt <= :toDate
            """)
    long countInRange(@Param("fromDate") java.time.LocalDateTime fromDate, @Param("toDate") java.time.LocalDateTime toDate);
}
