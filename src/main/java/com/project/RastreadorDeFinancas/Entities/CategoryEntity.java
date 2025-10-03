package com.project.RastreadorDeFinancas.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "TB_CATEGORY")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    public UUID ID;

    @Column(nullable = false)
    @Setter
    @Getter
    public String name;

    public CategoryEntity(String name){
        this.name = name;
    }

    public CategoryEntity(){

    }
}
