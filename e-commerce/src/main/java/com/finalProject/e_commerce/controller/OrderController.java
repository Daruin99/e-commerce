package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.OrderResponseDTO;
import com.finalProject.e_commerce.repository.OrderRepo;
import com.finalProject.e_commerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order/orders")
    public String getAllOrdersView(Model model, HttpServletRequest request){
        List<OrderResponseDTO> orderDTOs = orderService.getAllOrders();
        model.addAttribute("orderDTOs", orderDTOs);
        model.addAttribute("currentUri", request.getRequestURI());
        return "customer/viewAllOrders";
    }


    @GetMapping("/order/orders/{orderId}")
    public String getOrderView(Model model, HttpServletRequest request, @PathVariable("orderId") Long orderId){
        OrderResponseDTO orderDTO = orderService.getOrderById(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("currentUri", request.getRequestURI());
        return "customer/viewOrder";
    }
}
