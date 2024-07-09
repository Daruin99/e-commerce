package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepo extends JpaRepository<CustomerAddress,Long> {
}
