package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.config.PaymentServiceClient;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.CustomerAddress;
import com.finalProject.e_commerce.domain.Order;
import com.finalProject.e_commerce.domain.OrderItem;
import com.finalProject.e_commerce.dto.orderDTOs.OrderDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderItemDTO;
import com.finalProject.e_commerce.dto.orderDTOs.OrderResponseDTO;
import com.finalProject.e_commerce.dto.orderDTOs.PaymentRequestDTO;
import com.finalProject.e_commerce.exception.NotEnoughStockException;
import com.finalProject.e_commerce.repository.OrderRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.transaction.Transactional;
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
    private final CustomerAddressesService addressesService;
    private final ProductService productService;
    private final CartService cartService;


    public OrderService(OrderRepo orderRepository, PaymentServiceClient paymentServiceClient, MapperUtil mapperUtil, CustomerAddressesService addressesService, ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.paymentServiceClient = paymentServiceClient;
        this.mapperUtil = mapperUtil;
        this.addressesService = addressesService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Transactional
    public Long saveOrder(OrderDTO orderDTO) {

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            int currentStock = productService.getProductStock(orderItemDTO.getProduct().getId());
            if (currentStock < orderItemDTO.getQuantity()) {
                throw new NotEnoughStockException(orderItemDTO.getProduct().getName() + " is out of stock");
            }
        }

        CustomerAddress address = addressesService.getAddressById(orderDTO.getAddressId());

        Order order = mapperUtil.mapOrderDTOToEntity(orderDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();

        order.setCustomer(customer);
        order.setAddress(address.getStreet() + ", " + address.getResidentialArea() + ", " + address.getCity() + ", " + address.getCountry());
        order.setCreationDate(new Date());
        order.setOrderStatus("Completed");

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        if ("CreditCard".equals(orderDTO.getPaymentMethod())) {
            PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
            paymentRequestDTO.setCardNumber(orderDTO.getCardNumber());
            paymentRequestDTO.setAmount(orderDTO.getTotalPrice());

            ResponseEntity<String> paymentResponse = paymentServiceClient.processPayment(paymentRequestDTO);
            if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(paymentResponse.getBody());
            }
        }

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : order.getOrderItems()) {
            productService.updateStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }

        cartService.deleteAllByCustomerId(order.getCustomer().getId());

        return savedOrder.getId();
    }

    public List<OrderResponseDTO> getAllOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();

        List<Order> allOrders = orderRepository.findByCustomerIdOrderByCreationDateDesc(customer.getId());
        List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();

        for (Order order: allOrders){
            OrderResponseDTO orderDTO = mapperUtil.mapEntityToResponseDTO(order);
            List<OrderItemDTO> orderItemDTOS = mapperUtil.mapEntityToResponseDTO(order.getOrderItems());
            orderDTO.setOrderItems(orderItemDTOS);
            orderResponseDTOS.add(orderDTO);
        }

        return orderResponseDTOS;
    }

    public OrderResponseDTO getOrderById(Long orderId){

        Order order = orderRepository.findById(orderId).get();

        OrderResponseDTO orderDTO = mapperUtil.mapEntityToResponseDTO(order);

        List<OrderItemDTO> orderItemDTOS = mapperUtil.mapEntityToResponseDTO(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);


        return orderDTO;
    }
}
