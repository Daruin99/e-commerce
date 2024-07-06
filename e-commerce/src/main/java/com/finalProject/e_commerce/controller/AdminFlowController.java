package com.finalProject.e_commerce.controller;
import com.finalProject.e_commerce.dto.AdminRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminFlowController {

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/adminIndex";
    }

    @GetMapping("/admin/addAdmin")
    public String addAdmin(Model model) {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        model.addAttribute("AdminDTO", adminRequestDTO);
        return "admin/addAdmin";
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