package com.finalProject.e_commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/home";
    }
}