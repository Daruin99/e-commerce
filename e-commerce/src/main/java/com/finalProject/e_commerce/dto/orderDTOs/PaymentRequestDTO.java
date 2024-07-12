package com.finalProject.e_commerce.dto.orderDTOs;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String cardNumber;
    private double amount;
}
