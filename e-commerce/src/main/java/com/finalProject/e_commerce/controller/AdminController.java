package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.dto.AdminRequestDTO;
import com.finalProject.e_commerce.dto.AdminResponseDTO;
import com.finalProject.e_commerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/admins")
    public String viewAdmins(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        List<AdminResponseDTO> adminsResponse = adminService.getAdmins(pageNumber,field);
        model.addAttribute("adminsResponse", adminsResponse);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filed", field);
        return "admin/viewAdmins";
    }
    @PostMapping("/admin/addAdmin")
    public String addAdmin(@ModelAttribute("adminDTO") AdminRequestDTO adminRequestDTO){
        adminService.addAdmin(adminRequestDTO);

        return "redirect:/admin/admins";
    }

}
