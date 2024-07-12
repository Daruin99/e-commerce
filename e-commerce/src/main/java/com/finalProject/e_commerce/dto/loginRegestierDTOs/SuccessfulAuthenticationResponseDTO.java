package com.finalProject.e_commerce.dto.loginRegestierDTOs;

import lombok.Getter;

import java.util.Set;

@Getter
public class SuccessfulAuthenticationResponseDTO {
    private final Set<String> authorities;

    public SuccessfulAuthenticationResponseDTO(Set<String> authorities) {
        this.authorities = authorities;
    }
}
