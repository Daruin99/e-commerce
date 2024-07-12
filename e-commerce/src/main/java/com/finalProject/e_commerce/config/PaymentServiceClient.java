package com.finalProject.e_commerce.config;

import com.finalProject.e_commerce.dto.orderDTOs.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "payment-service", url = "http://localhost:8083/api/v1/payment")

public interface PaymentServiceClient {
    @PostMapping("/process")
    ResponseEntity<String> processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO);
}