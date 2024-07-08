package com.finalProject.e_commerce.dto.productDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private double price;

    @NotNull
    @Min(0)
    private int stock;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String description;

    @NotNull
    @Min(1)
    private Long categoryId;
}
