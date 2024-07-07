package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByNameAndCategory(String name, Category category);

    Page<Product> findByCategory(Category category, Pageable pageable);

}
