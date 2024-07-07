package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.service.AdminService;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MapperUtil mapper;

    @GetMapping("/admin/admins")
    public String getAllAdmins(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        List<AdminResponseDTO> adminsResponse = adminService.getAllAdmins(pageNumber,field);
        model.addAttribute("adminsResponse", adminsResponse);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filed", field);
        return "admin/viewAdmins";
    }

    @GetMapping("/admin/addAdmin")
    public String addAdminView(Model model) {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        model.addAttribute("AdminDTO", adminRequestDTO);
        return "admin/addAdmin";
    }

    @PostMapping("/admin/addAdmin")
    public String addAdminRequest(@ModelAttribute("adminDTO") AdminRequestDTO adminRequestDTO){
        adminService.addAdmin(adminRequestDTO);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admin/updateAdmin/{adminId}")
    public String updateAdminView(Model model, @PathVariable("adminId") Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        AdminResponseDTO adminResponseDTO = mapper.mapEntityToResponseDTO(admin);
        model.addAttribute("adminId", adminId);
        model.addAttribute("adminResponseDTO", adminResponseDTO);
        return "admin/updateAdmin";
    }

    @PostMapping("/admin/updateAdmin/{adminId}")
    public String updateAdminRequest(@PathVariable Long adminId, AdminRequestDTO adminRequestDTO) {
        adminService.updateAdmin(adminId, adminRequestDTO);
        return "redirect:/admin/admins";
    }

}
