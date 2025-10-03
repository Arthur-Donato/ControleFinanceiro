package com.project.RastreadorDeFinancas.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSACTION")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private UUID ID;

    @Column(nullable = false)
    @Setter
    @Getter
    private String type;

    @Column(nullable = false)
    @Setter
    @Getter
    private Double value;


    @Setter
    @Getter
    private String description;

    @Column(nullable = false)
    @Setter
    @Getter
    private Date date;

    public TransactionEntity(String type, Double value, String description, Date date){
        this.type = type;
        this.value = value;
        this.description = description;
        this.date = date;

    }

    public TransactionEntity(){

    }


}
