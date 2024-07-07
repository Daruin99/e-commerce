package com.finalProject.e_commerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/adminIndex";
    }

    @GetMapping("/admin/categories")
    public String viewCategories() {
        return "admin/viewCategories";
    }
}