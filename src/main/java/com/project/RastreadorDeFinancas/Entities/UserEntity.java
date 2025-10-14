package com.project.RastreadorDeFinancas.Entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
public class UserEntity extends RepresentationModel<UserEntity> implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    @Setter
    @Getter
    private UUID ID;


    @Column(nullable = false, unique = true)
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

    public UserEntity(@Nonnull String CPF, @Nonnull String name, @Nonnull String email, @Nonnull String password){
        this.CPF = CPF;
        this.name= name;
        this.email = email;
        this.password = password;
    }

    public UserEntity(){

    }


}
