package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean active;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;

        this.active = false;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public User activate() {
        this.active = true;
        return this;
    }

    public User inactivate() {
        this.active = false;
        return this;
    }

    public User grant(Role role) {
        if (role != null) {
            this.role = role;
        }
        return this;
    }

    public User revoke() {
        this.role = null;
        return this;
    }

    // UserDetails method implementations
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (getRole() != null) {
            return List.of(new SimpleGrantedAuthority(getRole().getCode()));
        }
        return List.of();
    }

    // getUsername(), getPassword() methods are overridden by lombok.

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
