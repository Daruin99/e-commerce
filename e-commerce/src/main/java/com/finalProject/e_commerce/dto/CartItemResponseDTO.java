package com.finalProject.e_commerce.dto;

import com.finalProject.e_commerce.domain.Cart;
import com.finalProject.e_commerce.domain.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CartItemResponseDTO {

    private Long id;

    private Product product;

    private int quantity;

    private double totalPrice;

    private Cart cart;
}
