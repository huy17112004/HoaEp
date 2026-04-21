package com.dearfloral.module.users.repository;

import com.dearfloral.module.users.entity.CustomerAddressEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddressEntity, Long> {
    List<CustomerAddressEntity> findByCustomerUserId(Long customerUserId);
    Optional<CustomerAddressEntity> findByIdAndCustomerUserId(Long id, Long customerUserId);

    @Modifying
    @Query("update CustomerAddressEntity a set a.isDefault = false where a.customerUser.id = :customerUserId")
    void resetDefaultByCustomerUserId(@Param("customerUserId") Long customerUserId);
}
