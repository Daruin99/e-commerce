package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepo customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepo customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }
}