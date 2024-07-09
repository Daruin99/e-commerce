package com.finalProject.e_commerce.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "items")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "total_price")
    private double totalPrice = 0;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
        calculateTotalPrice();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
        calculateTotalPrice();
    }

    public void clearCartItems() {
        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);
        }
        cartItems.clear();
        totalPrice = 0;
    }

    public void calculateTotalPrice() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getTotalPrice();
        }
        totalPrice = total;
    }
}
