package com.project.RastreadorDeFinancas.Dtos.Category;

import com.project.RastreadorDeFinancas.Entities.CategoryEntity;

import java.util.UUID;

public record CategoryResponseDto(String nameCategory, String nameUser, UUID idCateogry) {

    public CategoryResponseDto(CategoryEntity categoryEntity){
        this(categoryEntity.getName(), categoryEntity.getUserEntity().getName(), categoryEntity.getID());
    }
}
