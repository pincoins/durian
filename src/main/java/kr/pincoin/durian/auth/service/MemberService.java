package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.DocumentVerification;
import kr.pincoin.durian.auth.domain.PhoneVerification;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import kr.pincoin.durian.auth.controller.dto.UserCreateRequest;
import kr.pincoin.durian.auth.controller.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
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
    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<Profile>
    listMembers(UserStatus status) {
        return profileRepository.findMembers(status);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<Profile>
    getMember(Long userId, UserStatus status) {
        return profileRepository.findMember(userId, status);
    }

    @Transactional
    public Profile
    createMember(UserCreateRequest request) {
        User member = User.builder(request.getUsername(),
                                   passwordEncoder.encode(request.getPassword()),
                                   request.getFullName(),
                                   request.getEmail())
                .status(UserStatus.PENDING)
                .role(Role.MEMBER)
                .build();

        Profile profile = Profile.builder(member,
                                          new PhoneVerification(VerificationStatus.UNVERIFIED),
                                          new DocumentVerification(VerificationStatus.UNVERIFIED))
                .build();

        profileRepository.save(profile); // user entity is persisted in cascade.

        return profile;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public boolean
    deleteMember(Long userId) {
        return profileRepository.findMember(userId, Arrays.asList(UserStatus.PENDING,
                                                               UserStatus.INACTIVE,
                                                               UserStatus.UNREGISTERED))
                .map(profile -> {
                    profileRepository.delete(profile); // user entity is deleted in cascade.
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Member not found",
                                                      List.of("Member does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Profile>
    approveMember(Long userId) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.PENDING)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to approve.")));
        profile.getUser().approve();
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Profile>
    inactivateMember(Long userId) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to inactivate.")));

        profile.getUser().inactivate();
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<Profile>
    activateMember(Long userId) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.INACTIVE)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to activate.")));

        profile.getUser().activate();
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<Profile>
    unregisterMember(Long userId) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to unregister.")));

        profile.getUser().unregister();
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Profile>
    resetMemberPassword(Long userId,
                        UserResetPasswordRequest request) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to reset password.")));

        profile.getUser().changePassword(passwordEncoder.encode(request.getNewPassword()));
        return Optional.of(profile);
    }
}
