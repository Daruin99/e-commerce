package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.dto.orderDTOs.OrderDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderFailureResponseDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderSuccessResponseDTO;
import com.finalProject.e_commerce.exception.NotEnoughStockException;
import com.finalProject.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Long orderId = orderService.saveOrder(orderDTO);
            OrderSuccessResponseDTO successResponse = new OrderSuccessResponseDTO(orderId, "Order Placed Successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (NotEnoughStockException e) {
            OrderFailureResponseDTO failureResponse = new OrderFailureResponseDTO(e.getMessage());
            return new ResponseEntity<>(failureResponse, HttpStatus.OK);
        }

        catch (Exception e) {
            OrderFailureResponseDTO failureResponse = new OrderFailureResponseDTO("Failed To Place Order, Insufficient Balance");
            return new ResponseEntity<>(failureResponse, HttpStatus.OK);
        }
    }
}
