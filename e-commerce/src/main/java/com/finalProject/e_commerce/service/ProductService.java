package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductRequestDTO;
import com.finalProject.e_commerce.dto.productDTOs.ProductResponseDTO;
import com.finalProject.e_commerce.repository.CategoryRepo;
import com.finalProject.e_commerce.repository.ProductRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepository;
    private final MapperUtil mapper;

    public List<ProductResponseDTO> getAllProducts(int pageNumber, String field, Long categoryId){
        if (productRepository.findAll().isEmpty())
            return new ArrayList<>();

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        List<ProductResponseDTO> responseList = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).get();
            Page<Product> productPage = productRepository.findByCategory(category, pageable);

            List<Product> products = productPage.getContent();

            for (Product product : products) {
                responseList.add(mapper.mapEntityToResponseDTO(product));
            }
        } else {
            Page<Product> productPage = productRepository.findAll(pageable);

            List<Product> products = productPage.getContent();

            for (Product product : products) {
                responseList.add(mapper.mapEntityToResponseDTO(product));
            }
        }

        return responseList;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }
    public void addProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).get();

        if (productRepository.existsByNameAndCategory(productRequestDTO.getName(), category))
            return;

        Product newProduct = mapper.mapRequestDTOToEntity(productRequestDTO);
        newProduct.setCategory(category);

        productRepository.save(newProduct);
    }

    /*public void updateAdmin(Long adminId, AdminRequestDTO requestDTO){
        Admin admin = getAdminById(adminId);

        admin.setName(requestDTO.getName());
        admin.setEmail(requestDTO.getEmail());
        admin.setPhoneNumber(requestDTO.getPhoneNumber());

        productRepository.save(admin);
    }*/

}
