package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    Page<Admin> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE (a.email = :email OR a.phoneNumber = :phoneNumber) AND a.id <> :adminId")
    boolean existsByEmailOrPhoneNumberAndNotAdminId(@Param("email") String email, @Param("phoneNumber") String phoneNumber, @Param("adminId") Long adminId);


}