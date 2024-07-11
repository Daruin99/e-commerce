package com.finalProject.e_commerce.controller;

import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.domain.CustomerAddress;
import com.finalProject.e_commerce.dto.CartResponseDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressRequestDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressResponseDTO;
import com.finalProject.e_commerce.dto.addressDTOs.AddressUpdateDTO;
import com.finalProject.e_commerce.dto.adminDTOs.AdminUpdateDTO;
import com.finalProject.e_commerce.service.CartService;
import com.finalProject.e_commerce.service.CustomerAddressesService;
import com.finalProject.e_commerce.util.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class CustomerAddressesController {

    private final CartService cartService;
    private final CustomerAddressesService addressesService;
    private final MapperUtil mapper;

    @GetMapping("/customer/addresses")
    public String getAllAddresses(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber) {
        getAddressesLogic(model, request, pageNumber);
        CartResponseDTO cart = cartService.getCart();
        if(!cart.getCartItems().isEmpty()){
            int cartQuantity = 0;
            for(CartItem cartItem : cart.getCartItems()){
                cartQuantity = cartQuantity + cartItem.getQuantity();
            }
            model.addAttribute("cartQuantity", cartQuantity);
        }
        else {
            model.addAttribute("cartQuantity", 0);
        }
        return "customer/customerAddresses";
    }

    @GetMapping("/customer/confirmAddress")
    public String getAllAddressesToConfirm(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber) {
        getAddressesLogic(model, request, pageNumber);
        return "customer/confirm-address";
    }

    private void getAddressesLogic(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0", required = false, name = "pageNumber") int pageNumber) {
        Page<AddressResponseDTO> addressesDTO = addressesService.getAllAddresses(pageNumber);
        int totalPages = addressesDTO.getTotalPages();

        model.addAttribute("addressesResponseDTO", addressesDTO.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("addressRequestDTO", new AddressRequestDTO());
    }

    @PostMapping("/customer/addAddress")
    public String addAddressRequest(@Valid @ModelAttribute("addressRequestDTO") AddressRequestDTO addressRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "customer/confirm-address";
        }
        addressesService.addAddress(addressRequestDTO);
        return "redirect:/customer/confirm-address";
    }

    @PostMapping("/customer/addAddressAtConfirm")
    public String addAddressRequestToConfirm(@Valid @ModelAttribute("addressRequestDTO") AddressRequestDTO addressRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "customer/confirm-address";
        }
        addressesService.addAddress(addressRequestDTO);
        return "redirect:/customer/confirmAddress";
    }

    @GetMapping("/customer/updateAddress/{addressId}")
    public String updateAddressView(
            Model model,
            @PathVariable("addressId") Long addressId) {
        CustomerAddress address = addressesService.getAddressById(addressId);
        AddressUpdateDTO addressUpdateDTO = mapper.mapEntityToUpdateDTO(address);
        model.addAttribute("addressId", addressId);
        model.addAttribute("addressUpdateDTO", addressUpdateDTO);
        return "customer/updateAddress";
    }

    @PostMapping("/customer/updateAddress/{addressId}")
    public String updateAddressRequest(
            @PathVariable Long addressId,
            @Valid @ModelAttribute("addressUpdateDTO") AddressUpdateDTO addressUpdateDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "customer/updateAddress";
        }
        addressesService.updateAddress(addressId, addressUpdateDTO);
        return "redirect:/customer/addresses";
    }

    @GetMapping("/customer/deleteAddress/{addressId}")
    public String deleteAddress(@PathVariable("addressId") Long addressId) {
        addressesService.deleteAddress(addressId);

        return "redirect:/customer/addresses";
    }
}