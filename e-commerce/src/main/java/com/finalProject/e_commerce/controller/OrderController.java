package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.OrderDTO;
import com.finalProject.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestBody OrderDTO orderDTO) {
        System.out.println(orderDTO);
            String response = orderService.saveOrder(orderDTO);
            if (response.equals("Order placed successfully")) {
                return response;
            }
            return  response;

    }
}
