package com.project.RastreadorDeFinancas.Dtos.Response;

import com.project.RastreadorDeFinancas.Entities.CategoryEntity;

public record CategoryResponseDto(String nameCategory, String nameUser) {

    public CategoryResponseDto(CategoryEntity categoryEntity){
        this(categoryEntity.getName(), categoryEntity.getUserEntity().getName());
    }
}
