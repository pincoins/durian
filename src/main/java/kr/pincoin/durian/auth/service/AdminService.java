package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.controller.dto.UserChangeFullNameRequest;
import kr.pincoin.durian.auth.controller.dto.UserChangeUsernameRequest;
import kr.pincoin.durian.auth.controller.dto.UserCreateRequest;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.common.service.GoogleRecaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final GoogleRecaptchaService googleRecaptchaService;

    @PreAuthorize("hasRole('SYSADMIN')")
    public List<User>
    listAdmins(UserStatus status) {
        return userRepository.findAdmins(status);
    }

    @PreAuthorize("hasRole('SYSADMIN')")
    public Optional<User>
    getAdmin(Long userId, UserStatus status) {
        return userRepository.findAdmin(userId, status);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public User
    createAdmin(UserCreateRequest request) {
        if (googleRecaptchaService.isUnverified(request.getCaptcha())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Google reCAPTCHA code not verified",
                                   List.of("Your Google reCAPTCHA code is invalid."));
        }

        User admin = User.builder(request.getUsername(),
                                  passwordEncoder.encode(request.getPassword()),
                                  request.getFullName())
                .status(UserStatus.NORMAL)
                .role(Role.SYSADMIN)
                .build();

        userRepository.save(admin);

        return admin;
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN') and @identity.isOwner(#userId)")
    public boolean
    deleteAdmin(Long userId) {
        return userRepository.findAdmin(userId, UserStatus.NORMAL)
                .map(admin -> {
                    userRepository.delete(admin);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Admin not found",
                                                      List.of("Admin does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN') and @identity.isOwner(#userId)")
    public Optional<User>
    changeUsername(Long userId, UserChangeUsernameRequest request) {
        return userRepository.findAdmin(userId, UserStatus.NORMAL)
                .map(admin -> Optional.of(admin.changeUsername(request.getUsername())))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Admin not found",
                                                    List.of("Admin does not exist to change username.")));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN') and @identity.isOwner(#userId)")
    public Optional<User>
    changeFullName(Long userId, UserChangeFullNameRequest request) {
        return userRepository.findAdmin(userId, UserStatus.NORMAL)
                .map(admin -> Optional.of(admin.changeFullName(request.getFullName())))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Admin not found",
                                                    List.of("Admin does not exist to change full name.")));
    }
}
