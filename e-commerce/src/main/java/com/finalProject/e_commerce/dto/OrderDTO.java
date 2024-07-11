package com.finalProject.e_commerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private String paymentMethod;
    private String cardNumber;
    private Long addressId;
    private List<OrderItemDTO> orderItems;
    // to be added adress

    private double totalPrice;
    private double taxes;
    private double deliveryFees;

}