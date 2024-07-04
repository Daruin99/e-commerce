package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.repository.AdminRepo;
import com.finalProject.e_commerce.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final CustomerRepo customerRepo;
    private final AdminRepo adminRepo;


    @Autowired
    public UserDetailsServiceImp(CustomerRepo customerRepo, AdminRepo adminRepository) {
        this.customerRepo = customerRepo;
        this.adminRepo = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByEmail(email).orElse(null);
        if (admin != null) {
            return admin;
        }

        // Check for Customer if Admin is not found
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return customer;
    }
}