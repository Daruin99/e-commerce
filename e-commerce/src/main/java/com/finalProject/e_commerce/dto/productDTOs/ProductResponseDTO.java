package com.finalProject.e_commerce.dto.productDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;

    private String name;

    private double price;

    private int stock;

    private String imageUrl;

    private String description;

    private int numberOfSoldItems;

    private String categoryName;

}
