package com.project.RastreadorDeFinancas.Dtos;

import jakarta.annotation.*;

public record CreateUserDto(@Nonnull String CPF, @Nonnull String name, @Nonnull String email, @Nonnull String password) {
}
