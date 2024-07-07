package com.finalProject.e_commerce.repo;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByCategory(Category category);
}
