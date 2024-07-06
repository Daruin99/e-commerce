package com.finalProject.e_commerce.controller;

import org.springframework.security.core.GrantedAuthority;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.dto.AuthenticationRequestDTO;
import com.finalProject.e_commerce.dto.FailedAuthenticationResponseDTO;
import com.finalProject.e_commerce.dto.SuccessfulAuthenticationResponseDTO;
import com.finalProject.e_commerce.service.CustomerService;
import com.finalProject.e_commerce.service.UserDetailsServiceImp;
import com.finalProject.e_commerce.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {


    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsServiceImp userDetailsService;

    private CustomerService customerService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImp userDetailsService, CustomerService customerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authRequest, HttpServletResponse response) throws Exception {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getUsername());
        if (userDetails == null) {

            return ResponseEntity.status(401).body(new FailedAuthenticationResponseDTO("This email doesn't exist in the system", 3));

        }

        if (userDetails instanceof Customer) {
            Customer customer = (Customer) userDetails;
            if (!customer.isAccountNonLocked()) {
                //N.B this needs to change 3la 7asb h3ml reset ezai

                return ResponseEntity.status(403).body(new FailedAuthenticationResponseDTO("Your account has been locked. Please enter your email to reset your password.", 3 - customer.getFailedAttempts()));
            }
            if (!customer.isEnabled()) {
                return ResponseEntity.status(401).body(new FailedAuthenticationResponseDTO("Account not verified. Please check your mail", 3));

            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            if (userDetails instanceof Customer) {
                Customer customer = (Customer) userDetails;
                customer.setFailedAttempts(customer.getFailedAttempts() + 1);
                if (customer.getFailedAttempts() >= 3) {
                    customerService.updateCustomer(customer);
                    //N.B this needs to change 3la 7asb h3ml reset ezai
                    return ResponseEntity.status(403).body(new FailedAuthenticationResponseDTO("Your account has been locked. Please enter your email to reset your password.", 3 - customer.getFailedAttempts()));
                } else {
                    customerService.updateCustomer(customer);
                    return ResponseEntity.status(401).body(new FailedAuthenticationResponseDTO("failed login attempt", 3 - customer.getFailedAttempts()));
                }

            }
            return ResponseEntity.status(401).body(new FailedAuthenticationResponseDTO("Incorrect password", 3));
        }

        if (userDetails instanceof Customer) {
            Customer customer = (Customer) userDetails;
            customer.setFailedAttempts(0); // Reset failed attempts after successful login
            customerService.updateCustomer(customer);
        }


        final String jwt = jwtUtil.generateToken(userDetails);
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);

        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new SuccessfulAuthenticationResponseDTO(authorities));
    }
}
