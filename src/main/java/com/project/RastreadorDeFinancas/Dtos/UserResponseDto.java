package com.project.RastreadorDeFinancas.Dtos;

import com.project.RastreadorDeFinancas.Entities.UserEntity;

public record UserResponseDto(String name, String email, String CPF) {

    public UserResponseDto(UserEntity userEntity){
        this(userEntity.getName(), userEntity.getEmail(), userEntity.getCPF());
    }
}
