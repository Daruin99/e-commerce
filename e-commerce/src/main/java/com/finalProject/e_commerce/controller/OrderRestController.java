package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.OrderDTO;
import com.finalProject.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
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
