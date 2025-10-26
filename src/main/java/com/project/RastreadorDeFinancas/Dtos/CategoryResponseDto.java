package com.project.RastreadorDeFinancas.Dtos;

import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;

public record CategoryResponseDto(String nameCategory, String nameUser) {

    public CategoryResponseDto(CategoryEntity categoryEntity){
        this(categoryEntity.getName(), categoryEntity.getUserEntity().getName());
    }
}
