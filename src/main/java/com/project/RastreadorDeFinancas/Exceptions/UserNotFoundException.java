package com.project.RastreadorDeFinancas.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(){
        super("There aren`t any user with this ID");
    }
}
