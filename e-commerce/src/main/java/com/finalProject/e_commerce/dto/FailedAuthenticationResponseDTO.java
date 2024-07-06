package com.finalProject.e_commerce.dto;

import lombok.Data;

@Data
public class FailedAuthenticationResponseDTO {
    private String message;
    private int remainingAttempts;

    public FailedAuthenticationResponseDTO(String message, int remainingAttempts) {
        this.message = message;
        this.remainingAttempts = remainingAttempts;
    }
}
