package kr.pincoin.durian.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.controller.dto.*;
import kr.pincoin.durian.auth.domain.*;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.auth.repository.redis.EmailVerificationRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.common.service.GoogleRecaptchaService;
import kr.pincoin.durian.common.util.RandomString;
import kr.pincoin.durian.common.util.RequestHeaderParser;
import kr.pincoin.durian.notification.controller.dto.MailgunSendRequest;
import kr.pincoin.durian.notification.service.MailgunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final PasswordEncoder passwordEncoder;

    private final RequestHeaderParser requestHeaderParser;

    private final GoogleRecaptchaService googleRecaptchaService;

    private final MailgunService mailgunService;

    @Value("${auth.verification.email.timeout}")
    private long emailVerificationTimeout;

    @Value("${auth.verification.email.from}")
    private String emailVerificationFrom;

    @Value("${auth.verification.email.subject}")
    private String emailVerificationSubject;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<Profile>
    listMembers(UserStatus status) {
        return profileRepository.findMembers(status);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Profile>
    getMember(Long userId, UserStatus status) {
        log.warn("userId: {}", userId);
        return profileRepository.findMember(userId, status);
    }

    @Transactional
    public Profile
    createMember(UserCreateRequest request, HttpServletRequest servletRequest) {
        if (googleRecaptchaService.isUnverified(request.getCaptcha())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Google reCAPTCHA code not verified",
                                   List.of("Your Google reCAPTCHA code is invalid."));
        }

        RequestHeaderParser headerParser = requestHeaderParser.changeHttpServletRequest(servletRequest);

        String ipAddressAndEmail = String.format("%s|%s", headerParser.getIpAddress(), request.getUsername());

        return emailVerificationRepository
                .findById(ipAddressAndEmail)
                .map(emailVerification -> {
                    if (emailVerification.isVerified()) {
                        User member = User.builder(request.getUsername(),
                                                   passwordEncoder.encode(request.getPassword()),
                                                   request.getFullName())
                                .status(UserStatus.NORMAL)
                                .role(Role.MEMBER)
                                .build();

                        Profile profile = Profile.builder(VerificationStatus.VERIFIED,
                                                          new PhoneVerification(VerificationStatus.UNVERIFIED),
                                                          new DocumentVerification(VerificationStatus.UNVERIFIED))
                                .build();

                        profile.belongsTo(member);

                        profileRepository.save(profile); // user entity is persisted in cascade.

                        return profile;
                    } else {
                        throw new ApiException(HttpStatus.BAD_REQUEST,
                                               "Email is not verified",
                                               List.of("Please, verify your email address."));
                    }
                }).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                      "Email is not verified",
                                                      List.of("Your email verification could be expired.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
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
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
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
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
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

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Profile> changeUsername(Long userId, UserChangeUsernameRequest request) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to change username.")));

        profile.getUser().changeUsername(request.getUsername());
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Profile> changeFullName(Long userId, UserChangeFullNameRequest request) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to change full name.")));

        profile.getUser().changeFullName(request.getFullName());
        return Optional.of(profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Profile> updateFavorites(Long userId, Favorites request) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to update favorites.")));
        try {
            profile.updateFavorites(new ObjectMapper().writeValueAsString(request));

            return Optional.of(profile);
        } catch (JsonProcessingException ignored) {
            throw new ApiException(HttpStatus.CONFLICT,
                                   "Favorites JSON parse error",
                                   List.of("Favorites json format is invalid."));
        }
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Profile> updateCart(Long userId, Cart request) {
        Profile profile = profileRepository
                .findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to update cart.")));
        try {
            profile.updateCart(new ObjectMapper().writeValueAsString(request));

            return Optional.of(profile);
        } catch (JsonProcessingException ignored) {
            throw new ApiException(HttpStatus.CONFLICT,
                                   "Favorites JSON parse error",
                                   List.of("Favorites json format is invalid."));
        }
    }

    @Transactional
    public boolean
    sendVerificationEmail(EmailVerificationRequest request, HttpServletRequest servletRequest) {
        validateGoogleCaptchaAndUser(request);

        RequestHeaderParser headerParser = requestHeaderParser.changeHttpServletRequest(servletRequest);

        String ipAddressAndEmail = String.format("%s|%s", headerParser.getIpAddress(), request.getUsername());

        emailVerificationRepository
                .findById(ipAddressAndEmail)
                .ifPresent(emailVerification -> {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                                           "Email already sent",
                                           List.of("Please, check your mailbox."));
                });

        String code = new RandomString(RandomString.Type.NUMERIC, 6).randomString();

        try {
            mailgunService.send(new MailgunSendRequest(emailVerificationFrom,
                                                       request.getUsername(),
                                                       emailVerificationSubject,
                                                       code))
                    .ifPresentOrElse(result -> emailVerificationRepository
                                             .save(new EmailVerification(ipAddressAndEmail,
                                                                         headerParser.getUserAgent(),
                                                                         code)
                                                           .unverified()
                                                           .setTimeout(emailVerificationTimeout)),
                                     () -> {
                        throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                                               "Failed to send email",
                                               List.of("Your email address is invalid."));
                    });
        } catch (WebClientResponseException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                                   "Failed to send email",
                                   List.of("Email server was not configured correctly."));
        }

        return true;
    }

    @Transactional
    public boolean
    sendEmailCode(EmailCodeRequest request, HttpServletRequest servletRequest) {
        validateGoogleCaptchaAndUser(request);

        RequestHeaderParser headerParser = requestHeaderParser.changeHttpServletRequest(servletRequest);

        String ipAddressAndEmail = String.format("%s|%s", headerParser.getIpAddress(), request.getUsername());

        return emailVerificationRepository
                .findById(ipAddressAndEmail)
                .map(emailVerification -> {
                    if (emailVerification.getCode().equals(request.getCode())) {
                        emailVerificationRepository.save(emailVerification
                                                                 .verified()
                                                                 .setTimeout(emailVerificationTimeout));
                        return true;
                    }
                    return false;
                }).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                      "Access denied",
                                                      List.of("Email verification process has not begun.")));
    }

    private void validateGoogleCaptchaAndUser(EmailVerificationRequest request) {
        if (googleRecaptchaService.isUnverified(request.getCaptcha())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Invalid reCAPTCHA",
                                   List.of("Your Google reCAPTCHA code is invalid."));
        }

        userRepository
                .findUserByUsername(request.getUsername(), null)
                .ifPresent(u -> {
                    throw new ApiException(HttpStatus.CONFLICT,
                                           "Duplicated email address",
                                           List.of("Your email address is already registered."));
                });
    }
}
