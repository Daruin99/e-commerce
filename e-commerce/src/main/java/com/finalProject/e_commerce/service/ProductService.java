package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;


    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> findProductById(Long productId) {
        return productRepo.findById(productId);
    }

}
