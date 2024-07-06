package com.finalProject.e_commerce.dto;

import lombok.Getter;

@Getter
public class SuccessfulAuthenticationResponseDTO {
    private final String authority;

    public SuccessfulAuthenticationResponseDTO(String authority) {
        this.authority = authority;
    }


}
