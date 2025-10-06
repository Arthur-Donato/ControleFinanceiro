package com.project.RastreadorDeFinancas.Repository;


import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
}
