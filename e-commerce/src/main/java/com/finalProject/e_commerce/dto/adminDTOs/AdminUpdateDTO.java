package com.finalProject.e_commerce.dto.adminDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(
            regexp = "^(010|011|012|015)\\d{8}$",
            message = "Phone number must be a valid Egyptian phone number starting with 010, 011, 012, or 015 followed by 8 digits."
    )
    @NotNull
    private String phoneNumber;

}
