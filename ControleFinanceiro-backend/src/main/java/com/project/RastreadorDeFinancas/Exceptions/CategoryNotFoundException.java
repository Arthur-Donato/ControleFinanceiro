package com.project.RastreadorDeFinancas.Exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("There aren`t any categories with this ID");
    }
}
