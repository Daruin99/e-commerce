package com.finalProject.e_commerce.dto.addressDTOs;

import com.finalProject.e_commerce.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private Long id;
    private String street;
    private String residentialArea;
    private String city;
    private String country;
}
