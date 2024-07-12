
package com.finalProject.e_commerce.controller;

import java.util.List;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.dto.cartDTOs.CartItemResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.CustomerAddressesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @PostMapping("/addItem")
    public ResponseEntity<?> addItemToCart(@RequestParam Long productId, HttpServletRequest request) {
        return cartService.addItemToCart(productId);
    }

    @GetMapping("/items")

    public ResponseEntity<?> getCartItems() {
        System.out.println("fetching");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();

        List<CartItemResponseDTO> cartItems = cartService.getCartItems(customer.getId());
        System.out.println(cartItems.size());

        return ResponseEntity.ok(cartItems);

    }

}
