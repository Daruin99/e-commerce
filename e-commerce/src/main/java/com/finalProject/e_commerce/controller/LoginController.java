package com.finalProject.e_commerce.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "message", required = false) String message, HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (message == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (authority.getAuthority().equals("CUSTOMER")) {
                        return "redirect:/customer/home";
                    } else if (authority.getAuthority().equals("ADMIN")) {
                        return "redirect:/admin/home";
                    }
                }
            }
        }

        return "login";
    }

}