package com.finalProject.e_commerce.dto.addressDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUpdateDTO {

    private Long id;

    @NotBlank
    private String street;

    @NotBlank
    private String residentialArea;

    @NotBlank
    private String city;

    @NotBlank
    private String country;
}
