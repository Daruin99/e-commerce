package com.finalProject.e_commerce.dto;

import com.finalProject.e_commerce.dto.addressDTOs.AddressResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private List<OrderItemDTO> orderItems;

    private AddressResponseDTO address;

    private String paymentMethod;

    private Date creationDate;

    private String orderStatus;

    private double totalPrice;

    private double taxes;

    private double deliveryFees;
}
