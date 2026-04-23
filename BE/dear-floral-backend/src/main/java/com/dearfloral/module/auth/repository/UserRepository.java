package com.dearfloral.module.auth.repository;

import com.dearfloral.module.auth.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @EntityGraph(attributePaths = {"role"})
    Optional<UserEntity> findByEmail(String email);
    @EntityGraph(attributePaths = {"role"})
    Optional<UserEntity> findWithRoleById(Long id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
