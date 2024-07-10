package com.finalProject.e_commerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomerController {

    @GetMapping("/customer/payment")
    public String paymentView() {
        return "customer/payment";
    }

    @GetMapping("/customer/payment/{addressId}")
    public String paymentView(@PathVariable(name = "addressId") Long addressId) {
        return "customer/payment";
    }

}