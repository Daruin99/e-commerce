package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "order_id_fk"))
    private Order order;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "product_id_fk"))
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private double totalPrice;
}
