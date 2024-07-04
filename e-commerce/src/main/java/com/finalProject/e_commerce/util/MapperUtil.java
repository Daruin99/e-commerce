package com.finalProject.e_commerce.util;

import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.ProductRequestDTO;
import com.finalProject.e_commerce.dto.ProductResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperUtil {

    // To map the coming ProductRequestDTO to Product Entity to persist it in the db
    Product mapRequestDTOToEntity(ProductRequestDTO requestDTO);

    // to map the Product Entity to ProductResponseDTO to send it back in the response
    ProductResponseDTO mapEntityToResponseDTO(Product product);
}
