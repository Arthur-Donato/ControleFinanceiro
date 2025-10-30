package com.project.RastreadorDeFinancas.Repository;


import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    CategoryEntity getFirstByUserEntity(UserEntity user);


    @Query(value = "SELECT * FROM TB_CATEGORY t WHERE t.user_ID = :userID", nativeQuery = true)
    List<CategoryEntity> findAllByUserID(@Param("userID") UUID userID);

    @Query(value = "SELECT * FROM TB_CATEGORY t WHERE t.user_ID = :userID AND t.id = :id" ,
    nativeQuery = true)
    Optional<CategoryEntity> findByUserEntityAndID(@Param ("userID") UUID userID, @Param("id") UUID id);
}
