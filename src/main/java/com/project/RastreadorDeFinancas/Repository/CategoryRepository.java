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


    @Query(value = "SELECT * FROM TB_CATEGORY t WHERE t.userEntity LIKE %:userEntity%",
    nativeQuery = true)
    List<CategoryEntity> findAllByUserEntity(@Param("userEntity") UserEntity userEntity);

    @Query(value = "SELECT * FROM TB_CATEGORY t WHERE t.userEntity LIKE %:userEntity% AND t.id LIKE %:id%",
    nativeQuery = true)
    Optional<CategoryEntity> findByUserEntityAndID(@Param ("userEntity") UserEntity userEntity, @Param("id") UUID id);
}
