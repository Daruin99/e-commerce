package com.finalProject.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSuccessResponseDTO {
    private Long orderId;
    private String message;
}
