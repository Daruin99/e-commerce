package com.finalProject.e_commerce.dto;

public class PasswordResetRequestDTO {
    private String token;
    private String email;
    private String password;
    private String confirmPassword;

    public PasswordResetRequestDTO(String token, String password, String confirmPassword, String email) {
        this.token = token;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public PasswordResetRequestDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
