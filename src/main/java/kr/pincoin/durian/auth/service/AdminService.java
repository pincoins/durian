package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.repository.jpa.RoleRepository;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public List<User>
    listAdmins(UserStatus status) {
        return userRepository.findAdmins(status);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public Optional<User>
    getAdmin(Long userId, UserStatus status) {
        return userRepository.findAdmin(userId, status);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public UserResponse
    createAdmin(UserCreateRequest request) {
        log.warn("create admin service");
        return roleRepository.findRole("ROLE_SYSADMIN")
                .map(role -> {
                    User user = userRepository.save(new User(request.getUsername(),
                                                             passwordEncoder.encode(request.getPassword()),
                                                             request.getName(),
                                                             request.getEmail(),
                                                             UserStatus.NORMAL)
                                                            .grant(role));

                    return new UserResponse(user);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Role not found",
                                                    List.of("Role has to exists in order to create admin.")));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN') and @identity.isOwner(#userId)")
    public boolean
    deleteAdmin(Long userId) {
        return userRepository.findAdmin(userId, UserStatus.NORMAL)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Role not found",
                                                      List.of("Role does not exist to delete.")));
    }
}
