package com.project.RastreadorDeFinancas.Repository;

import com.project.RastreadorDeFinancas.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query(value = "SELECT * FROM TB_USERS t WHERE t.email = :email AND t.password = :password", nativeQuery = true)
    Optional<UserEntity> getUserEntityByEmailAndPassword(@Param(value = "email") String email, @Param(value = "password") String password);
}
