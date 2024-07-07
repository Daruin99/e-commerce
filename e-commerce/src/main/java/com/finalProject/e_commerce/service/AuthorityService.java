package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Authority;
import com.finalProject.e_commerce.repository.AuthorityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorityService {

    private final AuthorityRepo authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepo authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority findById(Long id) {
        return authorityRepository.findById(id).orElseThrow();
    }
}
