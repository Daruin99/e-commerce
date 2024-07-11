package com.finalProject.e_commerce.repository;


import com.finalProject.e_commerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByCreationDateDesc(Long customerId);

}