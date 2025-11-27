package com.project.RastreadorDeFinancas.Exceptions;

public class TransactionNotSavedException extends RuntimeException {
    public TransactionNotSavedException() {
        super("The transaction was not saved on database");
    }
}
