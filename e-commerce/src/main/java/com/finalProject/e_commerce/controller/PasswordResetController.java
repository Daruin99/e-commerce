package com.finalProject.e_commerce.controller;


import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.dto.loginRegestierDTOs.EmailResetRequestDTO;
import com.finalProject.e_commerce.dto.loginRegestierDTOs.PasswordResetRequestDTO;
import com.finalProject.e_commerce.dto.loginRegestierDTOs.PasswordResetResponseDTO;
import com.finalProject.e_commerce.service.CustomerService;
import com.finalProject.e_commerce.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final CustomerService customerService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/forgot-password")
    public String forgotPassword(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "forgot-password";
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> handleForgotPassword(@RequestBody EmailResetRequestDTO emailResetRequest) {
        String email = emailResetRequest.getEmail();
        if (customerService.existsByEmail(email)) {
            Customer customer = customerService.findByEmail(email);
            System.out.println("sending email");
            emailVerificationService.sendPasswordResetEmail(customer);
            return ResponseEntity.ok().body( new PasswordResetResponseDTO(true, "Verification email sent."));


        }
        return ResponseEntity.status(404).body(new PasswordResetResponseDTO(false, "This email doesn't exist in the system."));

    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, HttpServletRequest request, Model model) {
        model.addAttribute("request", new PasswordResetRequestDTO());
        model.addAttribute("currentUri", request.getRequestURI());
        String tokenStatus = emailVerificationService.verifyPasswordToken(token);
        if ("verificationCompleted".equals(tokenStatus)) {
            return "reset-password";
        } else if ("Invalid token".equals(tokenStatus)) {
            model.addAttribute("error", "Invalid token.");
            return "forgot-password";
        }
        model.addAttribute("error", "Expired token. please enter your email to be sent a new one.");
        return "forgot-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@Valid @ModelAttribute("request") PasswordResetRequestDTO request, Model model, BindingResult result) {
        if (result.hasErrors()){
            model.addAttribute("token", request.getToken());
            return "reset-password";
        }
        String email = request.getEmail();

        if (!customerService.existsByEmail(email)) {
            model.addAttribute("error", "Invalid email.");
            model.addAttribute("token", request.getToken());
            return "reset-password";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", request.getToken());
            return "reset-password";
        }

        // Update password
        customerService.updatePassword(email, request.getPassword());

        return "redirect:/login?message=succesfulReset";
    }
}
