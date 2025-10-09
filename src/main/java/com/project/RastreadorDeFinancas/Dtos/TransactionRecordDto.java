package com.project.RastreadorDeFinancas.Dtos;

import jakarta.annotation.Nonnull;

import java.util.Date;

public record TransactionRecordDto(@Nonnull String type, @Nonnull Double value, String description, @Nonnull Date date) {
}
