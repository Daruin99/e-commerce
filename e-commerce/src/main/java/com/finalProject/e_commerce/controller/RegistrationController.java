package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.customerDTOs.CustomerRequestDTO;
import com.finalProject.e_commerce.service.CustomerService;
import com.finalProject.e_commerce.service.EmailVerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Lazy
@Controller
public class RegistrationController {

    private final CustomerService customerService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public RegistrationController(@Lazy CustomerService customerService, @Lazy EmailVerificationService emailVerificationService) {
        this.customerService = customerService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customerDTO", new CustomerRequestDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("customerDTO") CustomerRequestDTO customerDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (customerService.existsByEmail(customerDTO.getEmail())) {
            model.addAttribute("error", "This user already exists");
            return "register";
        }
        customerService.saveCustomer(customerDTO);
        emailVerificationService.sendVerificationEmail(customerDTO.getEmail());
        return "redirect:/login?message=verificationSent";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        String result = emailVerificationService.verifyEmailToken(token);
        if ("expired token".equals(result)) {
            return "redirect:/login?message=expired";
        }
        return "redirect:/login?message=" + result;
    }
}
