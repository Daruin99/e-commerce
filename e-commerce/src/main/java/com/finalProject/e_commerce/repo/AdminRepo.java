package com.finalProject.e_commerce.repo;

import com.finalProject.e_commerce.domain.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

}