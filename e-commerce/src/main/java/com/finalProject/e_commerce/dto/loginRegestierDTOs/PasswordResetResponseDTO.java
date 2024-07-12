package com.finalProject.e_commerce.dto.loginRegestierDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponseDTO {

    private boolean success;
    private String message;
}
