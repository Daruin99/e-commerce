package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByCustomer(Customer customer);

}