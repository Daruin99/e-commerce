package com.finalProject.e_commerce.dto.orderDTOs;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Product;
import lombok.Data;

@Data
public class ProductRatingResponseDTO {
    private Long id;
    private Long productId;
    private Long customerId;
    private int rating;
}
