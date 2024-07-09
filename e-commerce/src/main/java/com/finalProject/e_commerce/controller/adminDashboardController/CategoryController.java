package com.finalProject.e_commerce.controller.adminDashboardController;

import com.finalProject.e_commerce.dto.categoryDTOs.CategoryRequestDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.service.adminDashboardServices.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String getAllCategories(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(pageNumber, field);

        int totalPages = categoriesDTO.getTotalPages();
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
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
            Model model) {
        if (result.hasErrors()) {
            Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(0, "id");
            model.addAttribute("categoriesDTO", categoriesDTO.getContent());
            model.addAttribute("categoryDTO", categoryRequestDTO);
            return "admin/viewCategories";
        }
        categoryService.addCategory(categoryRequestDTO);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/updateCategory/{categoryId}")
    public String updateCategoryRequest(
            @PathVariable Long categoryId,
            @Valid @ModelAttribute("categoryDTO") CategoryRequestDTO categoryRequestDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            Page<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategoriesPageable(0, "id");
            model.addAttribute("categoriesDTO", categoriesDTO.getContent());
            model.addAttribute("categoryDTO", categoryRequestDTO);
            return "admin/viewCategories";
        }
        categoryService.updateCategory(categoryId, categoryRequestDTO.getName());
        return "redirect:/admin/categories";
    }
}
