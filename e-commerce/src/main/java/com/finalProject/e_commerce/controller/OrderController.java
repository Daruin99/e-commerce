package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.dto.cartDTOs.CartResponseDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderItemDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderResponseDTO;
import com.finalProject.e_commerce.dto.orderDTOs.ProductRatingResponseDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.OrderService;
import com.finalProject.e_commerce.service.ProductRatingService;
import com.finalProject.e_commerce.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final CartService cartService;
    private final ProductRatingService productRatingService;


    @GetMapping("/order/orders")
    public String getAllOrdersView(Model model, HttpServletRequest request){
        List<OrderResponseDTO> orderDTOs = orderService.getAllOrders();
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
        model.addAttribute("orderDTOs", orderDTOs);
        model.addAttribute("currentUri", request.getRequestURI());
        return "customer/viewAllOrders";
    }


    @GetMapping("/order/orders/{orderId}")
    public String getOrderView(Model model, HttpServletRequest request, @PathVariable("orderId") Long orderId){
        OrderResponseDTO orderDTO = orderService.getOrderById(orderId);
        CartResponseDTO cart = cartService.getCart();
        List<ProductRatingResponseDTO> productRatings = productRatingService.getProductRatingsByCustomerId();
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
        for (OrderItemDTO item : orderDTO.getOrderItems()) {
            productRatings.stream()
                    .filter(pr -> pr.getProductId().equals(item.getProduct().getId()))
                    .findFirst()
                    .ifPresent(pr -> item.setCurrentRating(pr.getRating()));
        }
//        model.addAttribute("productRatings", productRatings);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("currentUri", request.getRequestURI());
        return "customer/viewOrder";
    }

    @PostMapping("/order/orders/{orderId}/rateProduct")
    public String rateProduct(@PathVariable("orderId") Long orderId, @RequestParam Long productId, @RequestParam int rating) {
        productService.addRating(productId, rating);
        return "redirect:/order/orders/" + orderId;
    }
}
