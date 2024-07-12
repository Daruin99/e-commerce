package com.finalProject.e_commerce.controller.adminDashboardController;

import com.finalProject.e_commerce.dto.categoryDTOs.CategoryRequestDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.service.adminDashboardServices.CategoryService;
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
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String getAllCategories(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(pageNumber, field);

        int totalPages = categoriesDTO.getTotalPages();
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("categoriesDTO", categoriesDTO.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("categoryDTO", categoryRequestDTO);
        model.addAttribute("totalPages", totalPages);
        return "admin/viewCategories";
    }

    @PostMapping("/admin/addCategory")
    public String addCategoryRequest(
            @Valid @ModelAttribute("categoryDTO") CategoryRequestDTO categoryRequestDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(0, "id");
            model.addAttribute("categoriesDTO", categoriesDTO.getContent());
            model.addAttribute("categoryDTO", categoryRequestDTO);
            return "admin/viewCategories";
        }
        if (categoryService.categoryExists(categoryRequestDTO.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "This Category Already Exists!");
            return "redirect:/admin/categories";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Category Added Successfully!.");
        categoryService.addCategory(categoryRequestDTO);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/updateCategory/{categoryId}")
    public String updateCategoryRequest(
            @PathVariable Long categoryId,
            @Valid @ModelAttribute("categoryDTO") CategoryRequestDTO categoryRequestDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(0, "id");
            model.addAttribute("categoriesDTO", categoriesDTO.getContent());
            model.addAttribute("categoryDTO", categoryRequestDTO);
            return "admin/viewCategories";
        }
        if (categoryService.categoryExistsForUpdate(categoryRequestDTO.getName(), categoryId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "This Category Details Already Exists!");
            return "redirect:/admin/categories";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Category Updated Successfully!.");
        categoryService.updateCategory(categoryId, categoryRequestDTO.getName());
        return "redirect:/admin/categories";
    }
}
