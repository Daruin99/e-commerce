package com.finalProject.e_commerce.controller.adminDashboardController;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminUpdateDTO;
import com.finalProject.e_commerce.service.adminDashboardServices.AdminService;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MapperUtil mapper;

    @GetMapping("/admin/admins")
    public String getAllAdmins(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field,
            @RequestParam(defaultValue = "", required = false, name = "name") String name
    ) {
        Page<AdminResponseDTO> adminsDTO = adminService.getAllAdmins(pageNumber, field, name);
        int totalPages = adminsDTO.getTotalPages();
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("adminsDTO", adminsDTO.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filed", field);
        model.addAttribute("name", name);
        model.addAttribute("totalPages", totalPages);
        return "admin/viewAdmins";
    }

    @GetMapping("/admin/addAdmin")
    public String addAdminView(Model model) {
        AdminRequestDTO adminDTO = new AdminRequestDTO();
        model.addAttribute("adminDTO", adminDTO);
        return "admin/addAdmin";
    }

    @PostMapping("/admin/addAdmin")
    public String addAdminRequest(
            @Valid @ModelAttribute("adminDTO") AdminRequestDTO adminDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/addAdmin";
        }
        if (adminService.adminExists(adminDTO.getEmail(), adminDTO.getPhoneNumber())) {
            redirectAttributes.addFlashAttribute("errorMessage", "This Admin Already Exists!");
            return "redirect:/admin/addAdmin";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Admin Added Successfully!.");
        adminService.addAdmin(adminDTO);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admin/updateAdmin/{adminId}")
    public String updateAdminView(
            Model model,
            @PathVariable("adminId") Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        AdminUpdateDTO adminUpdateDTO = mapper.mapEntityToUpdateDTO(admin);
        model.addAttribute("adminId", adminId);
        model.addAttribute("adminUpdateDTO", adminUpdateDTO);
        return "admin/updateAdmin";
    }

    @PostMapping("/admin/updateAdmin/{adminId}")
    public String updateAdminRequest(
            @PathVariable Long adminId,
            @Valid @ModelAttribute("adminUpdateDTO") AdminUpdateDTO adminUpdateDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/updateAdmin";
        }
        if (adminService.adminExistsForUpdate(adminUpdateDTO.getEmail(), adminUpdateDTO.getPhoneNumber(), adminId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "This Admin Details Already Exists!");
            return "redirect:/admin/updateAdmin/{adminId}";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Admin Updated successfully!");
        adminService.updateAdmin(adminId, adminUpdateDTO);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admin/deleteAdmin/{adminId}")
    public String deleteAdmin(@PathVariable("adminId") Long adminId, RedirectAttributes redirectAttributes) {
        adminService.deleteAdmin(adminId);
        redirectAttributes.addFlashAttribute("successMessage", "Admin Deleted Successfully!");
        return "redirect:/admin/admins";
    }

}
