package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {

    @Id
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    @Column(
            name = "id",
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "category_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String Name;
}
