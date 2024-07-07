package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.service.CategoryService;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.service.ProductService;
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
        List<ProductResponseDTO> productsResponse = productService.getAllProducts(pageNumber,field, categoryId);
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("productsResponse", productsResponse);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        return "customer/shop";
    }
}
