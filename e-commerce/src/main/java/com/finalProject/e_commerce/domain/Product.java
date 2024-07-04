package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
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
	private String Name;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "DOUBLE"
    )
	private double Price;

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
	private String Description;

    @Column(
            name = "number_of_sold_items",
            nullable = false,
            columnDefinition = "INT"
    )
	private int NumberOfSoldItems;

    @ManyToOne()
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "category_id_fk")
    )
	Category category;
}
