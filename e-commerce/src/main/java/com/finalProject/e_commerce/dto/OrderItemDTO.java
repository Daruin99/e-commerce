package com.finalProject.e_commerce.dto;

import com.finalProject.e_commerce.domain.Product;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Product product;
    private int quantity;
    private double totalPrice;
}