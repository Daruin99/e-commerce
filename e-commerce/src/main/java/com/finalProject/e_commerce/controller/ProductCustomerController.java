package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.CartResponseDTO;
import com.finalProject.e_commerce.dto.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductCustomerController {
//    @Autowired
    private final ProductService productService;
//    @Autowired
    private final CategoryService categoryService;
//    @Autowired
    private final CartService cartService;

    @GetMapping("/shop")
    public String getAllProducts(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        List<CategoryResponseDTO> categoriesResponse = categoryService.getAllCategories();
        List<ProductResponseDTO> productsResponse = productService.getAllProducts(pageNumber,field, categoryId);
        CartResponseDTO cartResponse = cartService.getCart();
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("productsResponse", productsResponse);
        model.addAttribute("cartItemsResponse", cartResponse.getCartItems());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        return "customer/shop";
    }

    @GetMapping("/products")
    public String getProductsByCategory(
            Model model,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "id", required = false, name = "field") String field) {
        List<ProductResponseDTO> productsResponse = productService.getAllProducts(pageNumber,field, categoryId);
        CartResponseDTO cartResponse = cartService.getCart();
        if(categoryId != null) {
            CategoryResponseDTO categoryResponse = categoryService.getCategoryById(categoryId);
            model.addAttribute("category", categoryResponse);
        }
        else{
            model.addAttribute("category", null);
        }
        model.addAttribute("productsResponse", productsResponse);
        model.addAttribute("cartItemsResponse", cartResponse.getCartItems());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        return "customer/category";
    }

    @GetMapping("/products/{id}")
    public String getProductById(
            Model model,
            @PathVariable Long id
    ) {
        ProductResponseDTO productResponse = productService.getProductResponseDTOById(id);
        model.addAttribute("productResponse", productResponse);
        return "customer/product";
    }
}
