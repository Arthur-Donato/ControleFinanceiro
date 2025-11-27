package com.project.RastreadorDeFinancas.Exceptions;

public class UserNotSavedException extends RuntimeException {
    public UserNotSavedException() {
        super("The user is not saved on database");
    }
}
