package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.domain.Authority;
import com.finalProject.e_commerce.dto.adminDTOs.AdminRequestDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminResponseDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminUpdateDTO;
import com.finalProject.e_commerce.repository.AdminRepo;
import com.finalProject.e_commerce.service.AuthorityService;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo repository;
    private final MapperUtil mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;

    public Page<AdminResponseDTO> getAllAdmins(int pageNumber, String field, String name) {

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));
        Page<Admin> adminPage;

        if(!name.isEmpty())
        {
            adminPage = repository.findByNameContaining(name, pageable);
        }
        else{
            adminPage = repository.findAll(pageable);
        }

        List<Admin> admins = adminPage.getContent();

        List<AdminResponseDTO> responseList = new ArrayList<>();

        for (Admin admin : admins){
            AdminResponseDTO adminResponseDTO = mapper.mapEntityToResponseDTO(admin);
            adminResponseDTO.setSuperAdmin(isSuperAdmin(admin));
            responseList.add(adminResponseDTO);
        }


        return new PageImpl<>(responseList, pageable, adminPage.getTotalElements());
    }

    public Admin getAdminById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
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

    public void updateAdmin(Long adminId, AdminUpdateDTO adminDTO) {
        Admin updatedAdmin = getAdminById(adminId);
        updatedAdmin.setName(adminDTO.getName());
        updatedAdmin.setEmail(adminDTO.getEmail());
        updatedAdmin.setPhoneNumber(adminDTO.getPhoneNumber());

        repository.save(updatedAdmin);
    }

    public void deleteAdmin(Long adminId) {
        repository.deleteById(adminId);
    }

    private boolean isSuperAdmin(Admin admin) {
        return admin.getAuthorities()
                .stream()
                .anyMatch(authority -> "SUPER_ADMIN".equals(authority.getAuthority()));
    }

    public boolean adminExists(String email, String phoneNumber) {
        return repository.existsByEmailOrPhoneNumber(email, phoneNumber);
    }
    public boolean adminExistsForUpdate(String email, String phoneNumber, Long adminId) {
        return  repository.existsByEmailOrPhoneNumberAndNotAdminId(email, phoneNumber, adminId);
    }
}
