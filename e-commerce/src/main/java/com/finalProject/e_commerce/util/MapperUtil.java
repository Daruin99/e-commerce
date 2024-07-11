package com.finalProject.e_commerce.util;

import com.finalProject.e_commerce.domain.*;
import com.finalProject.e_commerce.dto.*;
import com.finalProject.e_commerce.dto.addressDTOs.AddressRequestDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressResponseDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressUpdateDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminUpdateDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryRequestDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.customerDTOs.CustomerRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperUtil {

    // To map the coming ProductRequestDTO to Product Entity to persist it in the db
    Product mapRequestDTOToEntity(ProductRequestDTO requestDTO);

    // to map the Product Entity to ProductResponseDTO to send it back in the response
    ProductResponseDTO mapEntityToResponseDTO(Product product);

    ProductUpdateDTO mapEntityToUpdateDTO(Product product);

    Admin mapRequestDTOToEntity(AdminRequestDTO requestDTO);

    AdminResponseDTO mapEntityToResponseDTO(Admin admin);

    Category mapRequestDTOToEntity(CategoryRequestDTO requestDTO);

    CategoryResponseDTO mapEntityToResponseDTO(Category category);

    CartResponseDTO mapEntityToResponseDTO(Cart cart);
    AdminUpdateDTO mapEntityToUpdateDTO(Admin admin);

    Customer mapRequestDTOToEntity(CustomerRequestDTO customerDTO);

    AddressResponseDTO mapEntityToResponseDTO(CustomerAddress address);

    CustomerAddress mapRequestDTOToEntity(AddressRequestDTO addressRequestDTO);

    AddressUpdateDTO mapEntityToUpdateDTO(CustomerAddress address);

    CartItemResponseDTO mapEntityToDTO(CartItem cartItem);
    Order mapOrderDTOToEntity(OrderDTO orderDTO);
    OrderItem mapOrderItemDTOToEntity(OrderItemDTO orderItemDTO);

    OrderResponseDTO mapEntityToResponseDTO(Order order);

    List<OrderItemDTO> mapEntityToResponseDTO(List<OrderItem> orderItems);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "customer.id", target = "customerId")
    ProductRatingResponseDTO mapEntityToResponseDTO(ProductRating productRating);

}
