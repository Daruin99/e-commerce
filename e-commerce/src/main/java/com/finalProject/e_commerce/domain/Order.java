package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private CustomerAddress address;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "taxes")
    private double taxes;

    @Column(name = "delivery_fees")
    private double deliveryFees;
}
