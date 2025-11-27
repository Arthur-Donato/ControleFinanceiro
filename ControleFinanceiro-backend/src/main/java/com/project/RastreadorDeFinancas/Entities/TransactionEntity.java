package com.project.RastreadorDeFinancas.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSACTION")
public class TransactionEntity extends RepresentationModel<TransactionEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Setter
    @Getter
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_ID")
    @Setter
    @Getter
    private UserEntity userEntity;

    public TransactionEntity(String type, Double value, String description, Instant date){
        this.type = type;
        this.value = value;
        this.description = description;
        this.date = date;

    }

    public TransactionEntity(){

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(getID(), that.getID()) && Objects.equals(getType(), that.getType()) && Objects.equals(getValue(), that.getValue()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getCategoryEntity(), that.getCategoryEntity()) && Objects.equals(getUserEntity(), that.getUserEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getID(), getType(), getValue(), getDescription(), getDate(), getCategoryEntity(), getUserEntity());
    }
}
