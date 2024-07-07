package com.finalProject.e_commerce.controller;


import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.dto.PasswordResetRequestDTO;
import com.finalProject.e_commerce.service.CustomerService;
 import com.finalProject.e_commerce.dto.EmailResetRequestDTO;
import com.finalProject.e_commerce.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    private CustomerService customerService;
    private EmailVerificationService emailVerificationService;

    public PasswordResetController(CustomerService customerService, EmailVerificationService emailVerificationService) {
        this.customerService = customerService;
        this.emailVerificationService = emailVerificationService;
    }


    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestBody EmailResetRequestDTO emailResetRequest, Model model) {
        String email = emailResetRequest.getEmail();
        if (customerService.existsByEmail(email)) {
            Customer customer = customerService.findByEmail(email);
            emailVerificationService.sendPasswordResetEmail(customer);
            model.addAttribute("message", "Password reset email has been sent.");
        } else {
            model.addAttribute("error", "This email doesn't exist in the system.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, Model model) {
        String tokenStatus = emailVerificationService.verifyToken(token);
        if ("verificationCompleted".equals(tokenStatus)) {
            return "reset-password";
        } else {
            //N.B handle case eno invalid aw expired ka user ytl3lo eh
            model.addAttribute("error", "Invalid or expired token.");
            return "forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestBody PasswordResetRequestDTO request,
                                      Model model) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", request.getToken());
            return "reset-password";
        }

        String email = request.getEmail();
        System.out.println(email);
        if (customerService.existsByEmail(email)) {
            customerService.updatePassword(email, request.getPassword());
            model.addAttribute("message", "Password reset successfully, you can now log in with your new password.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid token.");
            return "forgot-password";
        }
    }
}
