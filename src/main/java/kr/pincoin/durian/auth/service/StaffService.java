package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.controller.dto.UserCreateRequest;
import kr.pincoin.durian.auth.controller.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.common.exception.ApiException;
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
public class StaffService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('SYSADMIN')")
    public List<User>
    listStaffs(UserStatus status) {
        return userRepository.findStaffs(status);
    }

    @PreAuthorize("hasRole('SYSADMIN')" +
            " or hasRole('STAFF') and @identity.isOwner(#userId)")
    public Optional<User>
    getStaff(Long userId, UserStatus status) {
        return userRepository.findStaff(userId, status);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public User
    createStaff(UserCreateRequest request) {
        User staff = User.builder(request.getUsername(),
                                  passwordEncoder.encode(request.getPassword()),
                                  request.getName(),
                                  request.getEmail())
                .status(UserStatus.NORMAL)
                .role(Role.STAFF)
                .build();

        userRepository.save(staff);

        return staff;
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')" +
            " or hasRole('STAFF') and @identity.isOwner(#userId)")
    public boolean
    deleteStaff(Long userId) {
        return userRepository.findStaff(userId, UserStatus.NORMAL)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Role not found",
                                                      List.of("Role does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public Optional<User>
    resetStaffPassword(Long userId,
                       UserResetPasswordRequest request) {
        User staff = userRepository
                .findStaff(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to reset password.")));

        staff.changePassword(passwordEncoder.encode(request.getNewPassword()));

        return Optional.of(staff);
    }
}
