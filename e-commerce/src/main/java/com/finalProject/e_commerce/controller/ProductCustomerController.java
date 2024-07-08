package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.service.adminDashboardServices.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.service.adminDashboardServices.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductCustomerController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/shop")
    public String getAllProducts(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        List<CategoryResponseDTO> categoriesResponse = categoryService.getAllCategories();
        Page<ProductResponseDTO> productsDTO;

        if (categoryId == null || categoryId == 0) {
            productsDTO = productService.getAllProducts(pageNumber, field, null);
        } else {
            productsDTO = productService.getAllProducts(pageNumber, field, categoryId);
        }
        int totalPages = productsDTO.getTotalPages();
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("productsResponse", productsDTO.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("totalPages", totalPages);
        return "admin/viewProducts";
    }
}
