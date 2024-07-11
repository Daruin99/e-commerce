package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.adminDashboardServices.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;


    @PostMapping("/addItem")
    public ResponseEntity<?> addItemToCart(@RequestParam Long productId, HttpServletRequest request) {
        return cartService.addItemToCart(productId);
    }

}
