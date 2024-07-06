package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Authority;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Lazy
@Service
public class CustomerService {

    private final CustomerRepo customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;

    @Autowired
    public CustomerService(CustomerRepo customerRepository, BCryptPasswordEncoder passwordEncoder, AuthorityService authorityService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
    }

    public void saveCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityService.findById(1L));
        customer.setAuthorities(authorities);
        customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}