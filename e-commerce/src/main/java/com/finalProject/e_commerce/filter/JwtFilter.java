package com.finalProject.e_commerce.filter;

import com.finalProject.e_commerce.util.JwtUtil;
import com.finalProject.e_commerce.repo.CustomerRepo;
import com.finalProject.e_commerce.repo.AdminRepo;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Admin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomerRepo customerRepo;
    private final AdminRepo adminRepo;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomerRepo customerRepo, AdminRepo adminRepo) {
        this.jwtUtil = jwtUtil;
        this.customerRepo = customerRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        String username = jwtUtil.getUsernameFromToken(token);
        Optional<? extends UserDetails> userDetailsOptional = getUserDetails(username);

        if (userDetailsOptional.isEmpty() || !jwtUtil.validateToken(token, userDetailsOptional.get())) {
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsOptional.get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Optional<? extends UserDetails> getUserDetails(String email) {
        Optional<Customer> customer = customerRepo.findByEmail(email);
        if (customer.isPresent()) {
            return customer;
        }

        Optional<Admin> admin = adminRepo.findByEmail(email);
        return admin;
    }
}
