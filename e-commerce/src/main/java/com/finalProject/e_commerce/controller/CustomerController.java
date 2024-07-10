package com.finalProject.e_commerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    @GetMapping("/customer/payment")
    public String paymentView() {
        return "customer/payment";
    }

    @GetMapping("/customer/confirmAddress")
    public String confirmAddressView() {
        return "customer/confirm-address";
    }
}