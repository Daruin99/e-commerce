package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.service.CategoryService;
import com.finalProject.e_commerce.service.ProductService;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/admin/products")
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
        return "admin/viewProducts";
    }

    @GetMapping("/admin/addProduct")
    public String addProductView(Model model) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        model.addAttribute("categories", categories);
        model.addAttribute("product", productRequestDTO);
        return "/admin/addProduct";
    }

    @PostMapping("/admin/addProduct")
    public String addAdminRequest(@ModelAttribute("productDTO") ProductRequestDTO productRequestDTO){
        productService.addProduct(productRequestDTO);
        return "redirect:/admin/products";
    }
}
