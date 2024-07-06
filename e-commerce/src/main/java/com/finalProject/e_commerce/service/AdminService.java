package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.Enum.Authority;
import com.finalProject.e_commerce.domain.Admin;
import com.finalProject.e_commerce.dto.AdminRequestDTO;
import com.finalProject.e_commerce.dto.AdminResponseDTO;
import com.finalProject.e_commerce.repo.AdminRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo repository;
    private final MapperUtil mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<AdminResponseDTO> getAdmins(int pageNumber, String field){
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

    public AdminResponseDTO getAdminById(Long id){
        Admin admin = repository.findById(id).orElse(null);
        return mapper.mapEntityToResponseDTO(admin);
    }

    public void addAdmin(AdminRequestDTO adminRequestDTO) {
        if (repository.existsByEmailOrPhoneNumber(adminRequestDTO.getEmail(), adminRequestDTO.getPhoneNumber()))
            return;

        String encodedPassword = passwordEncoder.encode(adminRequestDTO.getPassword());

        Admin newAdmin = mapper.mapRequestDTOToEntity(adminRequestDTO);
        newAdmin.setPassword(encodedPassword);
        newAdmin.setRole(Authority.valueOf("ADMIN"));

        repository.save(newAdmin);
    }
}
