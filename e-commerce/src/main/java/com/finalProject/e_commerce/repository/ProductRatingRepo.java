package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRatingRepo extends JpaRepository<ProductRating, Long> {
    Optional<ProductRating> findByProductIdAndCustomerId(Long productId, Long customerId);

    List<ProductRating> findAllByProductId(Long productId);

    List<ProductRating> findAllByCustomerId(Long customerId);
}
