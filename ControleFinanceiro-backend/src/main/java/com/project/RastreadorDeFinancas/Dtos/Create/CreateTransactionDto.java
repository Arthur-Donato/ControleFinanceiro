package com.project.RastreadorDeFinancas.Dtos.Create;

import jakarta.annotation.Nonnull;

import java.time.Instant;

public record CreateTransactionDto(@Nonnull String type, @Nonnull Double value, String description, @Nonnull Instant date) {
}
