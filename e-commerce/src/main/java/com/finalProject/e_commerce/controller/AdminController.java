package com.finalProject.e_commerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/adminIndex";
    }

    @GetMapping("/admin/admins")
    public String viewAdmins() {
        return "admin/viewAdmins";
    }

    @GetMapping("/admin/products")
    public String viewProducts() {
        return "admin/viewProducts";
    }

    @GetMapping("/admin/categories")
    public String viewCategories() {
        return "admin/viewCategories";
    }
}