package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.config.PaymentServiceClient;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.CustomerAddress;
import com.finalProject.e_commerce.domain.Order;
import com.finalProject.e_commerce.domain.OrderItem;
import com.finalProject.e_commerce.dto.OrderDTO;
import com.finalProject.e_commerce.dto.OrderItemDTO;
import com.finalProject.e_commerce.dto.OrderResponseDTO;
import com.finalProject.e_commerce.dto.PaymentRequestDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressResponseDTO;
import com.finalProject.e_commerce.repository.OrderRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepository;
    private final PaymentServiceClient paymentServiceClient;
    private final MapperUtil mapperUtil;


    @Autowired
    public OrderService(OrderRepo orderRepository, PaymentServiceClient paymentServiceClient, MapperUtil mapperUtil) {
        this.orderRepository = orderRepository;
        this.paymentServiceClient = paymentServiceClient;
        this.mapperUtil = mapperUtil;
    }

    @Transactional
    public String saveOrder(OrderDTO orderDTO) {
        // Create Order object from OrderDTO
        Order order = mapperUtil.mapOrderDTOToEntity(orderDTO);
        order.setCreationDate(new Date());
        System.out.println(order);
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));


        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Customer customer = (Customer) authentication.getPrincipal();
        //System.out.println(customer);

        //order.setCustomer(customer);


        if ("creditCard".equals(orderDTO.getPaymentMethod())) {
            PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
            paymentRequestDTO.setCardNumber(orderDTO.getCardNumber());
            paymentRequestDTO.setAmount(orderDTO.getTotalPrice());

            try {
                ResponseEntity<String> paymentResponse = paymentServiceClient.processPayment(paymentRequestDTO);
                System.out.println("success");
            } catch (RuntimeException exception) {
                System.out.println("Fail");
                return "no suffecient funds";
            }

        }

        // Save the order
        System.out.println("saving the order");
        orderRepository.save(order);

        // Update product stock and clear the cart
        /*
        for (OrderItem orderItem : order.getOrderItems()) {
            productRepository.updateStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            cartRepository.deleteByProductIdAndCustomerId(orderItem.getProduct().getId(), order.getCustomer().getId());
        }

         */

        return "Order placed successfully";
    }

    public List<OrderResponseDTO> getAllOrders(){

        List<Order> allOrders = orderRepository.findAll();
        List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();

        for (Order order: allOrders){
            OrderResponseDTO orderDTO = mapperUtil.mapEntityToResponseDTO(order);
            List<OrderItemDTO> orderItemDTOS = mapperUtil.mapEntityToResponseDTO(order.getOrderItems());
            AddressResponseDTO addressDTO = mapperUtil.mapEntityToResponseDTO(order.getAddress());
            orderDTO.setOrderItems(orderItemDTOS);
            orderDTO.setAddress(addressDTO);
            orderResponseDTOS.add(orderDTO);
        }

        return orderResponseDTOS;
    }

    public OrderResponseDTO getOrderById(Long orderId){

        Order order = orderRepository.findById(orderId).get();

        OrderResponseDTO orderDTO = mapperUtil.mapEntityToResponseDTO(order);

        List<OrderItemDTO> orderItemDTOS = mapperUtil.mapEntityToResponseDTO(order.getOrderItems());
        AddressResponseDTO addressDTO = mapperUtil.mapEntityToResponseDTO(order.getAddress());
        orderDTO.setOrderItems(orderItemDTOS);
        orderDTO.setAddress(addressDTO);

        return orderDTO;
    }
}
