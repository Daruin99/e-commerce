package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Category;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryRequestDTO;
import com.finalProject.e_commerce.dto.categoryDTOs.CategoryResponseDTO;
import com.finalProject.e_commerce.repository.CategoryRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepository;
    private final MapperUtil mapper;

    public Page<CategoryResponseDTO> getAllCategoriesPageable(int pageNumber, String field) {

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();

        List<CategoryResponseDTO> responseList = new ArrayList<>();

        for (Category category : categories)
            responseList.add(mapper.mapEntityToResponseDTO(category));

        return new PageImpl<>(responseList, pageable, categoryPage.getTotalElements());
    }

    public List<CategoryResponseDTO> getAllCategories() {
        if (categoryRepository.findAll().isEmpty())
            return new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseDTO> responseList = new ArrayList<>();

        for (Category category : categories)
            responseList.add(mapper.mapEntityToResponseDTO(category));

        return responseList;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = mapper.mapRequestDTOToEntity(categoryRequestDTO);

        categoryRepository.save(category);
    }

    public void updateCategory(Long categoryId, String categoryName) {
        Category updatedCategory = getCategoryById(categoryId);
        updatedCategory.setName(categoryName);

        categoryRepository.save(updatedCategory);
    }

    public boolean categoryExists(String name) {
        return categoryRepository.existsByName(name);
    }
    public boolean categoryExistsForUpdate(String name, Long categoryId) {
        return  categoryRepository.existsByNameAndNotId(name, categoryId);
    }
}
