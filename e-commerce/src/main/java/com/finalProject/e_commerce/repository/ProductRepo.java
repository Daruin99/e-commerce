package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.domain.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByNameAndCategory(String name, Category category);

    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryId(String name, Long categoryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity, p.numberOfSoldItems = p.numberOfSoldItems + :quantity WHERE p.id = :productId")
    void updateStock(Long productId, int quantity);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE (p.name = :name AND p.category = :category) AND p.id <> :productId")
    boolean existsByNameAndCategoryAndNotProductId(@Param("name") String name,@Param("category") Category category,@Param("productId") Long productId);
}
