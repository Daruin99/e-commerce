package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "customer_address")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "street",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String street;

    @Column(
            name = "residential_area",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String residentialArea;

    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String city;

    @Column(
            name = "country",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "customer_id_fk")
    )
    private Customer customer;
}
