package com.finalProject.e_commerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/unauthorized")
    public String unauthorized(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("CUSTOMER")) {
                    return "customer/customerError";
                } else if (authority.getAuthority().equals("ADMIN")) {
                    return "admin/adminError";
                }
            }
        }
        return "userError";
    }
}
