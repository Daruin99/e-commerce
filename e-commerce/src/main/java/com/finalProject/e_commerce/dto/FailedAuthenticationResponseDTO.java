package com.finalProject.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedAuthenticationResponseDTO {

    private String message;
    private int remainingAttempts;

}
