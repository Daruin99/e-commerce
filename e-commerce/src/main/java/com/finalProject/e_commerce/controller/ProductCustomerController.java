package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.CartResponseDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.CategoryCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.service.adminDashboardServices.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductCustomerController {
    private final ProductService productService;
    private final CategoryCustomerService categoryCustomerService;
    private final CartService cartService;

    @GetMapping("/shop")
    public String getAllProductsShop(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "idAsc", required = false, name = "field") String field) {
        List<CategoryResponseDTO> categoriesResponse = categoryCustomerService.getAllCategories();
        Page<ProductResponseDTO> productsResponse = productService.getAllProducts(pageNumber,field, categoryId);
        Page<ProductResponseDTO> mostSoldProductsResponse = productService.getAllProducts(pageNumber,"numberOfSoldItemsDesc", categoryId);
        CartResponseDTO cartResponse = cartService.getCart();
        int totalPages = productsResponse.getTotalPages();

        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("productsResponse", productsResponse.getContent());
        model.addAttribute("mostSoldProductsResponse", mostSoldProductsResponse.getContent());
        model.addAttribute("cartItemsResponse", cartResponse.getCartItems());
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("totalPages", totalPages);
        return "customer/shop";
    }

    @GetMapping("/products")
    public String getProductsByCategory(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "idAsc", required = false, name = "field") String field) {
        Page<ProductResponseDTO> productsResponse;
        List<CategoryResponseDTO> categoriesResponse = categoryCustomerService.getAllCategories();
        CartResponseDTO cartResponse = cartService.getCart();
        if(categoryId != null) {
            CategoryResponseDTO categoryResponse = categoryCustomerService.getCategoryById(categoryId);
            model.addAttribute("category", categoryResponse);
            productsResponse = productService.getAllProducts(pageNumber, field, categoryId);
        }
        else{
            model.addAttribute("category", null);
            productsResponse = productService.getAllProducts(pageNumber, field, null);
        }
        int totalPages = productsResponse.getTotalPages();
        model.addAttribute("productsResponse", productsResponse);
        model.addAttribute("cartItemsResponse", cartResponse.getCartItems());
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("totalPages", totalPages);
        return "customer/viewProducts";
    }

    @GetMapping("/products/{id}")
    public String getProductById(
            Model model,
            @PathVariable Long id
    ) {
        ProductResponseDTO productResponse = productService.getProductResponseDTOById(id);
        CartResponseDTO cartResponse = cartService.getCart();
        model.addAttribute("productResponse", productResponse);
        model.addAttribute("cartItemsResponse", cartResponse.getCartItems());
        return "customer/product";
    }
}
