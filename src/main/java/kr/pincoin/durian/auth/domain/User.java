package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(String username, String password, String name, String email, UserStatus status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public User approve() {
        this.status = UserStatus.NORMAL;
        return this;
    }

    public User activate() {
        this.status = UserStatus.NORMAL;
        return this;
    }

    public User inactivate() {
        this.status = UserStatus.INACTIVE;
        return this;
    }

    public User unregister() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.username = uuid;
        this.password = "";
        this.name = uuid;
        this.email = uuid;
        this.status = UserStatus.UNREGISTERED;
        return this;
    }

    public User grant(Role role) {
        this.role = role;
        return this;
    }

    public User revoke() {
        this.role = null;
        return this;
    }

    // UserDetails method implementations
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role.toString().isBlank()) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", role)));
    }

    // getUsername(), getPassword() methods are overridden by lombok.

    @Override
    public boolean isAccountNonExpired() {
        return getStatus() == UserStatus.NORMAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus() == UserStatus.NORMAL;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getStatus() == UserStatus.NORMAL;
    }

    @Override
    public boolean isEnabled() {
        return getStatus() == UserStatus.NORMAL;
    }
}
