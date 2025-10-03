package com.project.RastreadorDeFinancas.Dtos;

import jakarta.annotation.*;

public record UserRecordDto(@Nonnull String CPF, @Nonnull String name, @Nonnull String email, @Nonnull String password) {
}
