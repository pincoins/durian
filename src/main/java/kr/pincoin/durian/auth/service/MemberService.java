package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.DocumentVerification;
import kr.pincoin.durian.auth.domain.PhoneVerification;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.PhoneVerifiedStatus;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserProfileResult;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<UserProfileResult>
    listMembers(UserStatus status) {
        return userRepository.findMembers(status);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<UserProfileResult>
    getMember(Long userId, UserStatus status) {
        return userRepository.findMember(userId, status);
    }

    @Transactional
    public UserProfileResult
    createMember(UserCreateRequest request) {
        User member = userRepository.save(new User(request.getUsername(),
                                                   passwordEncoder.encode(request.getPassword()),
                                                   request.getName(),
                                                   request.getEmail(),
                                                   UserStatus.NORMAL)
                                                  .grant(Role.MEMBER));

        Profile profile = profileRepository.save(new Profile(member,
                                                             new PhoneVerification(false,
                                                                                   PhoneVerifiedStatus.UNVERIFIED),
                                                             new DocumentVerification(false)));

        return new UserProfileResult(member, profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public boolean
    deleteMember(Long userId) {
        return userRepository.findMember(userId, Arrays.asList(UserStatus.PENDING,
                                                               UserStatus.INACTIVE,
                                                               UserStatus.UNREGISTERED))
                .map(result -> {
                    profileRepository.deleteByUserId(userId);
                    userRepository.deleteById(userId);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Member not found",
                                                      List.of("Member does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<UserProfileResult>
    approveMember(Long userId) {
        UserProfileResult result = userRepository
                .findMember(userId, UserStatus.PENDING)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to approve.")));
        User user = result.getUser();
        user.approve();

        return Optional.of(result);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<UserProfileResult>
    inactivateMember(Long userId) {
        UserProfileResult result = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to inactivate.")));

        User user = result.getUser();
        user.inactivate();

        return Optional.of(result);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<UserProfileResult>
    activateMember(Long userId) {
        UserProfileResult result = userRepository
                .findMember(userId, UserStatus.INACTIVE)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to activate.")));

        User user = result.getUser();
        user.activate();

        return Optional.of(result);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<UserProfileResult>
    unregisterMember(Long userId) {
        UserProfileResult result = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to unregister.")));

        User user = result.getUser();
        user.unregister();

        return Optional.of(result);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<UserProfileResult>
    resetMemberPassword(Long userId,
                        UserResetPasswordRequest request) {
        UserProfileResult result = userRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to reset password.")));

        User user = result.getUser();
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));

        return Optional.of(result);
    }
}
