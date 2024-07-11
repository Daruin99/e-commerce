package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.domain.ProductRating;
import com.finalProject.e_commerce.dto.ProductRatingResponseDTO;
import com.finalProject.e_commerce.repository.ProductRatingRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRatingService {

    private final ProductRatingRepo productRatingRepo;
    private final MapperUtil mapper;

    public List<ProductRatingResponseDTO> getProductRatingsByCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        List<ProductRating> productRatings = productRatingRepo.findAllByCustomerId(customer.getId());
        List<ProductRatingResponseDTO> responseList = new ArrayList<>();

        for(ProductRating productRating : productRatings) {
            responseList.add(mapper.mapEntityToResponseDTO(productRating));
        }

        return responseList;

    }

}
