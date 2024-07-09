package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.repository.CategoryRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryCustomerService {

    private final CategoryRepo categoryRepository;
    private final MapperUtil mapper;

    public List<CategoryResponseDTO> getAllCategoriesPageable(int pageNumber, String field) {
            if (categoryRepository.findAll().isEmpty())
                return new ArrayList<>();

            int pageSize = 3;

            if (pageNumber <= 0)
                pageNumber = 0;

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));

            Page<Category> categoryPage = categoryRepository.findAll(pageable);

            List<Category> categories = categoryPage.getContent();

            List<CategoryResponseDTO> responseList = new ArrayList<>();

            for(Category category: categories)
                responseList.add(mapper.mapEntityToResponseDTO(category));

            return responseList;
    }

    public List<CategoryResponseDTO> getAllCategories(){
        if (categoryRepository.findAll().isEmpty())
            return new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseDTO> responseList = new ArrayList<>();

        for(Category category: categories)
            responseList.add(mapper.mapEntityToResponseDTO(category));

        return responseList;
    }

    public CategoryResponseDTO getCategoryById(Long categoryId) {
        return mapper.mapEntityToResponseDTO(categoryRepository.findById(categoryId).orElse(null));
    }
}
