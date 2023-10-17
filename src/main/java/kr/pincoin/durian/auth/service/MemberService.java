package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
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
public class MemberService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(UserRepository userRepository,
                         RoleRepository roleRepository,
                         ProfileRepository profileRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<User>
    listMembers(UserStatus status) {
        return userRepository.findMembers(status);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<User>
    getMember(Long userId, UserStatus status) {
        return userRepository.findMember(userId, status);
    }

    @Transactional
    public UserResponse
    createMember(UserCreateRequest request) {
        return roleRepository.findRole("ROLE_MEMBER")
                .map(role -> {
                    User member = userRepository
                            .save(new User(request.getUsername(),
                                           passwordEncoder.encode(
                                                   request.getPassword()),
                                           request.getName(),
                                           request.getEmail(),
                                           UserStatus.PENDING)
                                          .grant(role));

                    profileRepository.save(new Profile(member));

                    return new UserResponse(member);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Role not found",
                                                    List.of("Role has to exists in order to create member.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public boolean
    deleteMember(Long userId) {
        return userRepository.findMember(userId, UserStatus.INACTIVE)
                .map(member -> {
                    profileRepository.deleteByUserId(member.getId());
                    userRepository.delete(member);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Role not found",
                                                      List.of("Role does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<User>
    approveMember(Long userId) {
        User member = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to inactivate.")));

        return Optional.of(userRepository.save(member.inactivate()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<User>
    inactivateMember(Long userId) {
        User member = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to inactivate.")));

        return Optional.of(userRepository.save(member.inactivate()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<User>
    activateMember(Long userId) {
        User member = userRepository
                .findMember(userId, UserStatus.INACTIVE)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to activate.")));

        return Optional.of(userRepository.save(member.activate()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<User>
    unregisterMember(Long userId) {
        User member = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to unregister.")));

        return Optional.of(userRepository.save(member.unregister()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<User>
    resetMemberPassword(Long userId,
                        UserResetPasswordRequest request) {
        User member = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to reset password.")));

        member.changePassword(passwordEncoder.encode(request.getNewPassword()));

        return Optional.of(userRepository.save(member));
    }
}
