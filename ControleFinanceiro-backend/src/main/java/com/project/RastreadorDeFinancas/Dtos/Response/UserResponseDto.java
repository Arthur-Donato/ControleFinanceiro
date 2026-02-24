package com.project.RastreadorDeFinancas.Dtos.Response;

import com.project.RastreadorDeFinancas.Entities.UserEntity;

import java.util.UUID;

public record UserResponseDto(String name, String email, UUID idUser) {

    public UserResponseDto(UserEntity userEntity){
        this(userEntity.getName(), userEntity.getEmail(), userEntity.getID());
    }
}
