package com.finalProject.e_commerce.Enum;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    CUSTOMER,
    ADMIN,
    SUPER_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}