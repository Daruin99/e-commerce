package com.finalProject.e_commerce.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String cardNumber;
    private double amount;
}
