package com.project.RastreadorDeFinancas.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TB_USERS")
public class UserEntity {

    @Id
    @Setter
    @Getter
    private String CPF;

    @Column(nullable = false)
    @Setter
    @Getter
    private String name;

    @Column(nullable = false, unique = true)
    @Setter
    @Getter
    private String email;

    @Column(nullable = false)
    @Setter
    @Getter
    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_CPF")
    @Setter
    @Getter
    private List<TransactionEntity> transactions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_CPF")
    @Setter
    @Getter
    private List<CategoryEntity> categories;

    public UserEntity(String CPF, String name, String email, String password){
        this.CPF = CPF;
        this.name= name;
        this.email = email;
        this.password = password;
    }

    public UserEntity(){

    }


}
