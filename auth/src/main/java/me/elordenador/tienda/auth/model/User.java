package me.elordenador.tienda.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @Column(name = "password_version", nullable = false)
    @Getter @Setter
    private int passwordVersion = 1;

    @Column(nullable = false, length = 150)
    @Getter @Setter
    private String fullname;

    @Column(nullable = false, length = 150, unique = true)
    @Getter @Setter
    private String username;

    @Column(name = "is_admin", nullable = false)
    @Getter @Setter
    private boolean admin = false;

    @Column(name = "is_seller", nullable = false)
    @Getter @Setter
    private boolean seller = false;

    @Column(nullable = false)
    @Getter @Setter
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (this.admin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (this.seller) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        }
        return authorities;
    }

    public void incrementPasswordVersion() {
        this.passwordVersion++;
    }
}
