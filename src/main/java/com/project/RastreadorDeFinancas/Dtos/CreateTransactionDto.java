package com.project.RastreadorDeFinancas.Dtos;

import jakarta.annotation.Nonnull;

import java.time.Instant;
import java.util.Date;

public record CreateTransactionDto(@Nonnull String type, @Nonnull Double value, String description, @Nonnull Instant date) {
}
