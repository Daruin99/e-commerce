package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
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
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "customer/cart";
    }

    @PostMapping("/addItem")
    public String addItemToCart(@RequestParam Long productId) {
        cartService.addItemToCart(productId);
        return "redirect:/shop";
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
