package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "auth_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class User extends BaseDateTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username") // email address as a username
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Profile> profiles = new ArrayList<>();

    public static UserBuilder builder(String username,
                                      String password,
                                      String fullName) {
        return new UserBuilder()
                .username(username)
                .password(password)
                .fullName(fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User member = (User) o;
        return id != null && Objects.equals(id, member.id) && Objects.equals(username, member.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    // OneToOne cannot be set due to eager loading.
    public void add(Profile profile) {
        if (!profiles.contains(profile)) {
            this.profiles.add(profile);
        }

        if (profile.getUser() != this) {
            profile.belongsTo(this);
        }
    }

    public User changePassword(String password) {
        this.password = password;
        return this;
    }

    public User changeUsername(String username) {
        this.username = username;
        return this;
    }

    public User changeFullName(String fullName) {
        this.fullName = fullName;
        return this;
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
        String uuid = UUID.randomUUID().toString();
        this.username = uuid;
        this.password = "";
        this.fullName = uuid;
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

    // getUsername(), getPassword() methods are already overridden by Lombok.

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
