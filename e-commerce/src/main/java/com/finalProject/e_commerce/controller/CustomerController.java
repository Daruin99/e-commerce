package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.dto.cartDTOs.CartResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CartService cartService;

    @GetMapping("/customer/payment")
    public String paymentView() {
        return "customer/payment";
    }

    @GetMapping("/customer/payment/{addressId}")
    public String paymentView(@PathVariable(name = "addressId") Long addressId, Model model) {
        CartResponseDTO cart = cartService.getCart();
        if(!cart.getCartItems().isEmpty()){
            int quantity = 0;
            for(CartItem cartItem : cart.getCartItems()){
                quantity = quantity + cartItem.getQuantity();
            }
            model.addAttribute("cartQuantity", quantity);
        }
        else {
            model.addAttribute("cartQuantity", 0);
        }
        return "customer/payment";
    }

}