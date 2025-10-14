package com.project.RastreadorDeFinancas.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_CATEGORY")
public class CategoryEntity extends RepresentationModel<CategoryEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    @Setter
    @Getter
    public UUID ID;

    @Column(nullable = false)
    @Setter
    @Getter
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Setter
    @Getter
    private UserEntity userEntity;

    public CategoryEntity(String name){
        this.name = name;
    }

    public CategoryEntity(){

    }
}
