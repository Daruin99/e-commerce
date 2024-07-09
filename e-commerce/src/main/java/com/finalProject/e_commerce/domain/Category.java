package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category", uniqueConstraints = {
        @UniqueConstraint(name = "name_unique_key", columnNames = "name")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String name;
}
