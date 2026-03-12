package com.project.RastreadorDeFinancas.Dtos.Transaction;

import com.project.RastreadorDeFinancas.Entities.TransactionEntity;

import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDto(Double value, String type, String description, String categoryName, Instant date, UUID idTransaction) {

    public TransactionResponseDto(TransactionEntity transactionEntity){
        this(transactionEntity.getValue(), transactionEntity.getType(), transactionEntity.getDescription(), transactionEntity.getCategoryEntity().getName(), transactionEntity.getDate(), transactionEntity.getID());
    }
}
