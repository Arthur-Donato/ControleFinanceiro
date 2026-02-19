package com.project.RastreadorDeFinancas.Dtos.Create;

import jakarta.annotation.*;

public record   CreateUserDto(@Nonnull String name, @Nonnull String email, @Nonnull String password) {
}
