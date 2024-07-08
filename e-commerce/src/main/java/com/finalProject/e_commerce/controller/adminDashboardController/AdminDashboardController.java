package com.finalProject.e_commerce.controller.adminDashboardController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/adminIndex";
    }

}