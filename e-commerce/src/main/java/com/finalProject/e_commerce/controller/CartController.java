package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.dto.CartResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.domain.Cart;
import com.finalProject.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String getCart(Model model) {
        CartResponseDTO cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "customer/cart";
    }

    @PostMapping("/addItem")
    public String addItemToCart(@RequestParam Long productId, HttpServletRequest request) {
        cartService.addItemToCart(productId);
        String referrer = request.getHeader("referer");
        return "redirect:" + referrer;
    }

    @PostMapping("/removeItem")
    public String removeItemFromCart(@RequestParam Long productId){
        cartService.removeItemFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/updateItem")
    public String updateItemQuantity(@RequestParam Long productId, @RequestParam Integer quantity) {
        cartService.updateItemQuantity(productId, quantity);
        return "redirect:/cart";
    }

}
