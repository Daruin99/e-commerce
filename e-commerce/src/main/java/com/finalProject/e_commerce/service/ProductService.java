package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.domain.ProductRating;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductUpdateDTO;
import com.finalProject.e_commerce.repository.CategoryRepo;
import com.finalProject.e_commerce.repository.ProductRatingRepo;
import com.finalProject.e_commerce.repository.ProductRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepository;
    private final ProductRatingRepo productRatingRepo;
    private final MapperUtil mapper;

    public Page<ProductResponseDTO> getAllProducts(int pageNumber, String field, Long categoryId) {

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        List<ProductResponseDTO> responseList = new ArrayList<>();
        Page<Product> productPage;
        Sort sort;

        if (field.endsWith("Desc")) {
            sort = Sort.by(field.substring(0, field.length() - 4)).descending();
        } else {
            sort = Sort.by(field.substring(0, field.length() - 3)).ascending();
        }


        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).get();
            productPage = productRepository.findByCategory(category, pageable);

            List<Product> products = productPage.getContent();

            for (Product product : products) {
                responseList.add(mapper.mapEntityToResponseDTO(product));
            }
        } else {
            productPage = productRepository.findAll(pageable);

            List<Product> products = productPage.getContent();

            for (Product product : products) {
                responseList.add(mapper.mapEntityToResponseDTO(product));
            }
        }

        return new PageImpl<>(responseList, pageable, productPage.getTotalElements());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public ProductResponseDTO getProductResponseDTOById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return mapper.mapEntityToResponseDTO(product);
    }
    public Page<ProductResponseDTO> searchProducts(int pageNumber, String field, String name, Long categoryId) {

        int pageSize = 3;
        if (pageNumber <= 0) pageNumber = 0;

        Page<Product> productPage;
        Sort sort;

        if (field.endsWith("Desc")) {
            sort = Sort.by(field.substring(0, field.length() - 4)).descending();
        } else {
            sort = Sort.by(field.substring(0, field.length() - 3)).ascending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (categoryId != null) {
            productPage = productRepository.findByNameContainingAndCategoryId(name, categoryId, pageable);
        } else {
            productPage = productRepository.findByNameContaining(name, pageable);
        }

        List<Product> products = productPage.getContent();
        List<ProductResponseDTO> responseList = new ArrayList<>();

        for (Product product : products) {
            responseList.add(mapper.mapEntityToResponseDTO(product));
        }

        return new PageImpl<>(responseList, pageable, productPage.getTotalElements());
    }

    public Page<ProductResponseDTO> getFilteredProducts(int pageNumber, String field, String name, Long categoryId) {

        int pageSize = 3;
        if (pageNumber <= 0) pageNumber = 0;

        List<ProductResponseDTO> responseList = new ArrayList<>();
        Page<Product> productPage;
        Sort sort;

        if (field.endsWith("Desc")) {
            sort = Sort.by(field.substring(0, field.length() - 4)).descending();
        } else {
            sort = Sort.by(field.substring(0, field.length() - 3)).ascending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (name != null && !name.isEmpty() && categoryId != null) {
            productPage = productRepository.findByNameContainingAndCategoryId(name, categoryId, pageable);
        } else if (name != null && !name.isEmpty()) {
            productPage = productRepository.findByNameContaining(name, pageable);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            productPage = productRepository.findByCategory(category, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<Product> products = productPage.getContent();
        for (Product product : products) {
            responseList.add(mapper.mapEntityToResponseDTO(product));
        }

        return new PageImpl<>(responseList, pageable, productPage.getTotalElements());
    }


    public void addProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).get();

        if (productRepository.existsByNameAndCategory(productRequestDTO.getName(), category))
            return;

        Product newProduct = mapper.mapRequestDTOToEntity(productRequestDTO);
        newProduct.setCategory(category);

        productRepository.save(newProduct);
    }

    public void updateProduct(Long adminId, ProductUpdateDTO productDTO) {
        Product updatedProduct = getProductById(adminId);
        Category category = categoryRepository.findById(productDTO.getCategoryId()).get();
        updatedProduct.setName(productDTO.getName());
        updatedProduct.setPrice(productDTO.getPrice());
        updatedProduct.setStock(productDTO.getStock());
        updatedProduct.setImageUrl(productDTO.getImageUrl());
        updatedProduct.setCategory(category);
        updatedProduct.setDescription(productDTO.getDescription());

        productRepository.save(updatedProduct);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void addRating(Long productId, int rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        Optional<ProductRating> existingRating = productRatingRepo.findByProductIdAndCustomerId(productId, customer.getId());
        if(existingRating.isPresent()){
            ProductRating productRating = existingRating.get();
            productRating.setRating(rating);
            productRatingRepo.save(productRating);
        }
        else {
            ProductRating productRating = new ProductRating();
            productRating.setProduct(productRepository.findById(productId).get());
            productRating.setCustomer(customer);
            productRating.setRating(rating);
            productRatingRepo.save(productRating);
        }

        updateProductAverageRating(productId);
    }
    private void updateProductAverageRating(Long productId) {
        Product product = productRepository.findById(productId).get();
        double averageRating = productRatingRepo.findAllByProductId(productId)
                .stream()
                .mapToInt(ProductRating::getRating)
                .average()
                .orElse(0.0);
        product.setAverageRating(Math.round(averageRating));
        productRepository.save(product);
    }

    public void updateStock(Long productId, int quantity) {
        productRepository.updateStock(productId, quantity);
    }

    public int getProductStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product.getStock();
    }

    public boolean productExists(String name, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        return productRepository.existsByNameAndCategory(name, category);
    }
    public boolean productExistsForUpdate(String name, Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId).get();
        return  productRepository.existsByNameAndCategoryAndNotProductId(name, category, productId);
    }
}
