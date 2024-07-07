package com.finalProject.e_commerce.dto;

public class EmailResetRequestDTO {
    private String email;

    public EmailResetRequestDTO() {
    }

    public EmailResetRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
