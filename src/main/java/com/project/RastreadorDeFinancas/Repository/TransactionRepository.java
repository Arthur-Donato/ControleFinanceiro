package com.project.RastreadorDeFinancas.Repository;

import com.project.RastreadorDeFinancas.Entities.TransactionEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByUserEntity(UserEntity userEntity);

    @Query(value = "SELECT * FROM TB_TRANSACTION t WHERE t.userEntity LIKE %:userEntity% AND t.ID LIKE %:id%"
    , nativeQuery = true)
    Optional<TransactionEntity> findByUserEntityAndID(@Param("userEntity") UserEntity userEntity, @Param("id") UUID id);
}
