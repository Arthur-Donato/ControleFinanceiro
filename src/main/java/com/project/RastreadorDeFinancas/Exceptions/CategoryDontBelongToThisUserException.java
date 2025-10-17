package com.project.RastreadorDeFinancas.Exceptions;

public class CategoryDontBelongToThisUserException extends RuntimeException {
    public CategoryDontBelongToThisUserException() {
        super("This category don`t belong to this actual user");
    }
}
