package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.dto.cartDTOs.CartResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import com.finalProject.e_commerce.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart")
    public String getCart(HttpServletRequest request, Model model) {
        CartResponseDTO cart = cartService.getCart();
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("cart", cart);
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
        return "customer/cart";
    }

//    @PostMapping("/addItem")
//    public String addItemToCart(@RequestParam Long productId, HttpServletRequest request) {
//        cartService.addItemToCart(productId);
//        String referrer = request.getHeader("referer");
//        return "redirect:" + referrer;
//    }

    @PostMapping("/removeItem")
    public String removeItemFromCart(@RequestParam Long productId){
        cartService.removeItemFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/updateItem")
    public String updateItemQuantity(Model model, @RequestParam Long productId, @RequestParam(defaultValue = "1") Integer quantity, HttpServletRequest request) {
        int success = cartService.updateItemQuantity(productId, quantity);
        CartResponseDTO cart = cartService.getCart();

        if(!cart.getCartItems().isEmpty()){
            int cartQuantity = 0;
            for(CartItem cartItem : cart.getCartItems()){
                cartQuantity = cartQuantity + cartItem.getQuantity();
            }
            model.addAttribute("cartQuantity", cartQuantity);
        }
        else {
            model.addAttribute("cartQuantity", 0);
        }
        if(success != -1 && success != -2) {
            model.addAttribute("stock", success);
        }
        else {
            model.addAttribute("stock", null);
        }
        model.addAttribute("currentUpdate", productId);
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("cart", cart);
        return "customer/cart";
    }

}
