package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.CustomerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressesRepo extends JpaRepository<CustomerAddress,Long> {

    Page<CustomerAddress> findByCustomer(Customer customer, Pageable pageable);

}
