package com.finalProject.e_commerce.dto.adminDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;
}
