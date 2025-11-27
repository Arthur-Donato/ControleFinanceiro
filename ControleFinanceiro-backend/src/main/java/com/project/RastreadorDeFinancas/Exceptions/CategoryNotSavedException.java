package com.project.RastreadorDeFinancas.Exceptions;

public class CategoryNotSavedException extends RuntimeException {
    public CategoryNotSavedException() {
        super("The category was not saved on database");
    }
}
