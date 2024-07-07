package com.finalProject.e_commerce.repo;

import com.finalProject.e_commerce.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId);
}
