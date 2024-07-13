package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Authority;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.dto.customerDTOs.CustomerRequestDTO;
import com.finalProject.e_commerce.repository.CustomerRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Lazy
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;
    private final MapperUtil mapper;

    public void saveCustomer(CustomerRequestDTO customerDTO) {
        Customer customer = mapper.mapRequestDTOToEntity(customerDTO);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        System.out.println(customer);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityService.findById(1L));
        customer.setAuthorities(authorities);
        customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean existsByphoneNumber(String phoneNumber) {
        return customerRepository.existsByphoneNumber(phoneNumber);
    }


    public void updatePassword(String email, String newPassword) {
        Customer customer = findByEmail(email);
        customer.setPassword(passwordEncoder.encode(newPassword));
        customer.setFailedAttempts(0);
        updateCustomer(customer);
    }
}