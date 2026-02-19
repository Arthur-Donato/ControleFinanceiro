package com.project.RastreadorDeFinancas.Dtos.Response;

import com.project.RastreadorDeFinancas.Entities.TransactionEntity;

import java.time.Instant;

public record TransactionResponseDto(Double value, String type, String description, String categoryName, Instant date) {

    public TransactionResponseDto(TransactionEntity transactionEntity){
        this(transactionEntity.getValue(), transactionEntity.getType(), transactionEntity.getDescription(), transactionEntity.getCategoryEntity().getName(), transactionEntity.getDate());
    }
}
