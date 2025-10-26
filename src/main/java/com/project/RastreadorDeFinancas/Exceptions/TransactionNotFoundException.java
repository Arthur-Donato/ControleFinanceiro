package com.project.RastreadorDeFinancas.Exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("There aren`t any transactions with this ID");
    }
}
