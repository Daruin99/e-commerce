package com.finalProject.e_commerce.dto;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.domain.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;

    private List<CartItem> cartItems = new ArrayList<>();

    private Customer customer;

    private double totalPrice;
}
