package com.finalProject.e_commerce.domain;

import com.finalProject.e_commerce.Enum.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "admin", uniqueConstraints = {
        @UniqueConstraint(name = "email_unique_key", columnNames = "email"),
        @UniqueConstraint(name = "phone_unique_key", columnNames = "phone_number")
})
public class Admin implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String email;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String name;

    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String phoneNumber;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(60)"
    )
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_authorities",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
