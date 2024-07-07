package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.domain.Authority;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.repository.AdminRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo repository;
    private final MapperUtil mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;

    public List<AdminResponseDTO> getAllAdmins(int pageNumber, String field){
        if (repository.findAll().isEmpty())
            return new ArrayList<>();

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));

        Page<Admin> adminPage = repository.findAll(pageable);

        List<Admin> admins = adminPage.getContent();

        List<AdminResponseDTO> responseList = new ArrayList<>();

        for(Admin admin: admins)
            responseList.add(mapper.mapEntityToResponseDTO(admin));

        return responseList;
    }

    public Admin getAdminById(Long id){
        return repository.findById(id).orElse(null);
    }
    public void addAdmin(AdminRequestDTO adminRequestDTO) {
        if (repository.existsByEmailOrPhoneNumber(adminRequestDTO.getEmail(), adminRequestDTO.getPhoneNumber()))
            return;

        String encodedPassword = passwordEncoder.encode(adminRequestDTO.getPassword());

        Admin newAdmin = mapper.mapRequestDTOToEntity(adminRequestDTO);
        newAdmin.setPassword(encodedPassword);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityService.findById(2L));
        newAdmin.setAuthorities(authorities);

        repository.save(newAdmin);
    }

    public void updateAdmin(Long adminId, AdminRequestDTO requestDTO){
        Admin admin = getAdminById(adminId);

        admin.setName(requestDTO.getName());
        admin.setEmail(requestDTO.getEmail());
        admin.setPhoneNumber(requestDTO.getPhoneNumber());

        repository.save(admin);
    }
}
