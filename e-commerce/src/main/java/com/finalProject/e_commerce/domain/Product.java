package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @ManyToOne()
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "category_id_fk")
    )
    Category category;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            columnDefinition = "BIGINT"
    )
    private Long id;
    @Column(
            name = "product_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String name;
    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "DOUBLE"
    )
    private double price;
    @Column(
            name = "stock",
            nullable = false,
            columnDefinition = "INT"
    )
    private int stock;
    @Column(
            name = "image_url",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String imageUrl;
    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String description;
    @Column(
            name = "number_of_sold_items",
            nullable = false,
            columnDefinition = "INT DEFAULT 0"
    )

    private int numberOfSoldItems = 0;
}
