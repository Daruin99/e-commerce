
package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import java.util.List;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.CustomerAddress;
import com.finalProject.e_commerce.dto.CartItemResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.CustomerAddressesService;
import com.finalProject.e_commerce.service.adminDashboardServices.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    private final CustomerAddressesService customerAddressesService;


    @PostMapping("/addItem")
    public ResponseEntity<?> addItemToCart(@RequestParam Long productId, HttpServletRequest request) {
        return cartService.addItemToCart(productId);
//        String referrer = request.getHeader("referer");
//        return ResponseEntity.status(400).body("Bad request");
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
