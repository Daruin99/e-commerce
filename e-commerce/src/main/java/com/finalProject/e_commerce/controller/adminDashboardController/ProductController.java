package com.finalProject.e_commerce.controller.adminDashboardController;

import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductUpdateDTO;
import com.finalProject.e_commerce.service.CategoryCustomerService;
import com.finalProject.e_commerce.service.adminDashboardServices.CategoryService;
import com.finalProject.e_commerce.service.adminDashboardServices.ProductService;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryCustomerService categoryCustomerService;
    private final MapperUtil mapper;

    @GetMapping("/admin/products")
    public String getAllProducts(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            @RequestParam(defaultValue = "", required = false, name = "name") String name,
            @RequestParam(defaultValue = "idAsc", required = false, name = "field") String field) {
        List<CategoryResponseDTO> categoriesResponse = categoryService.getAllCategories();
        Page<ProductResponseDTO> productsDTO = productService.getFilteredProducts(pageNumber, field, name, categoryId);

        if(categoryId != null) {
            CategoryResponseDTO categoryResponse = categoryCustomerService.getCategoryById(categoryId);
            model.addAttribute("category", categoryResponse);
        }
        else{
            model.addAttribute("category", null);
        }
        int totalPages = productsDTO.getTotalPages();

        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("categories", categoriesResponse);
        model.addAttribute("productsResponse", productsDTO.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("totalPages", totalPages);
        return "admin/viewProducts";
    }

    @GetMapping("/admin/addProduct")
    public String addProductView(Model model) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        model.addAttribute("categoriesDTO", categories);
        model.addAttribute("productDTO", productRequestDTO);
        return "/admin/addProduct";
    }

    @PostMapping("/admin/addProduct")
    public String addAdminRequest(@Valid @ModelAttribute("productDTO") ProductRequestDTO productRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/addProduct";
        }
        productService.addProduct(productRequestDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/updateProduct/{productId}")
    public String updateProductView(
            Model model,
            @PathVariable("productId") Long productId) {
        Product product = productService.getProductById(productId);
        ProductUpdateDTO productUpdateDTO = mapper.mapEntityToUpdateDTO(product);
        List<CategoryResponseDTO> categoriesDTO = categoryService.getAllCategories();
        model.addAttribute("productId", productId);
        model.addAttribute("categoriesDTO", categoriesDTO);
        model.addAttribute("productUpdateDTO", productUpdateDTO);
        return "admin/updateProduct";
    }

    @PostMapping("/admin/updateProduct/{productId}")
    public String updateProductRequest(
            @PathVariable Long productId,
            @Valid @ModelAttribute("productUpdateDTO") ProductUpdateDTO productUpdateDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/updateProduct";
        }
        productService.updateProduct(productId, productUpdateDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);

        return "redirect:/admin/products";
    }
}
