package com.finalProject.e_commerce.service.adminDashboardServices;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductUpdateDTO;
import com.finalProject.e_commerce.repository.CategoryRepo;
import com.finalProject.e_commerce.repository.ProductRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepository;
    private final MapperUtil mapper;

    public Page<ProductResponseDTO> getAllProducts(int pageNumber, String field, Long categoryId) {

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        List<ProductResponseDTO> responseList = new ArrayList<>();
        Page<Product> productPage;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));
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
    public void addProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).get();

        if (productRepository.existsByNameAndCategory(productRequestDTO.getName(), category))
            return;

        Product newProduct = mapper.mapRequestDTOToEntity(productRequestDTO);
        newProduct.setCategory(category);

        productRepository.save(newProduct);
    }

    public void updateAdmin(Long adminId, ProductUpdateDTO productDTO) {
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

}
