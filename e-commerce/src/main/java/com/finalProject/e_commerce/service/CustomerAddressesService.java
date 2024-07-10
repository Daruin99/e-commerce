package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.*;
import com.finalProject.e_commerce.dto.addressDTOs.AddressRequestDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressResponseDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressUpdateDTO;
import com.finalProject.e_commerce.repository.CustomerAddressesRepo;
import com.finalProject.e_commerce.repository.CustomerRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAddressesService {

    private final CustomerAddressesRepo addressesRepo;
    private final CustomerRepo customerRepo;
    private final MapperUtil mapper;

    public Page<AddressResponseDTO> getAllAddresses(int pageNumber) {

        int pageSize = 3;

        if (pageNumber <= 0)
            pageNumber = 0;

        List<AddressResponseDTO> responseList = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

        Customer customer = customerRepo.findById(getLoggedInCustomerId()).get();

        Page<CustomerAddress> addressesPage = addressesRepo.findByCustomer(customer, pageable);

        List<CustomerAddress> addresses = addressesPage.getContent();

        for (CustomerAddress address : addresses) {
            responseList.add(mapper.mapEntityToResponseDTO(address));
        }

        return new PageImpl<>(responseList, pageable, addressesPage.getTotalElements());
    }

    public CustomerAddress getAddressById(Long id) {
        return addressesRepo.findById(id).orElse(null);
    }

    public void addAddress(AddressRequestDTO addressRequestDTO) {
        Customer customer = customerRepo.findById(getLoggedInCustomerId()).get();

        CustomerAddress newAddress = mapper.mapRequestDTOToEntity(addressRequestDTO);
        newAddress.setCustomer(customer);

        addressesRepo.save(newAddress);
    }

    public void updateAddress(Long addressId, AddressUpdateDTO addressRequestDTO) {
        CustomerAddress updatedAddress = getAddressById(addressId);
        updatedAddress.setCity(addressRequestDTO.getCity());
        updatedAddress.setStreet(addressRequestDTO.getStreet());
        updatedAddress.setCountry(addressRequestDTO.getCountry());
        updatedAddress.setResidentialArea(addressRequestDTO.getResidentialArea());

        addressesRepo.save(updatedAddress);
    }

    public void deleteAddress(Long addressId) {
        addressesRepo.deleteById(addressId);
    }


    private Long getLoggedInCustomerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();

        return customer.getId();
    }
}
