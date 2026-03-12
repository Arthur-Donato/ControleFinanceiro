package com.project.RastreadorDeFinancas.Dtos.User;

import jakarta.annotation.*;

public record   CreateUserDto(@Nonnull String name, @Nonnull String email, @Nonnull String password) {
}
