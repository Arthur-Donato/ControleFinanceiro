package com.project.RastreadorDeFinancas.Dtos.Response;

import com.project.RastreadorDeFinancas.Entities.UserEntity;

public record UserResponseDto(String name, String email) {

    public UserResponseDto(UserEntity userEntity){
        this(userEntity.getName(), userEntity.getEmail());
    }
}
