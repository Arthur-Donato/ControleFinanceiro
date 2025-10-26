package com.project.RastreadorDeFinancas.Dtos;

import com.project.RastreadorDeFinancas.Entities.TransactionEntity;

import java.util.Date;

public record TransactionResponseDto(Double value, String type, String description, String categoryName, Date date) {

    public TransactionResponseDto(TransactionEntity transactionEntity){
        this(transactionEntity.getValue(), transactionEntity.getType(), transactionEntity.getDescription(), transactionEntity.getCategoryEntity().getName(), transactionEntity.getDate());
    }
}
