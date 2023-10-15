package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.RefreshToken;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.dto.*;
import kr.pincoin.durian.auth.jwt.TokenProvider;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.auth.repository.jpa.RoleRepository;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.auth.repository.redis.RefreshTokenRepository;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.auth.jwt.TokenProvider.ACCESS_TOKEN_EXPIRES_IN;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ProfileRepository profileRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       ProfileRepository profileRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       TokenProvider tokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse
    createUser(UserCreateRequest request) {
        return roleRepository.findRole("ROLE_USER")
                .map(role -> {
                    User user = userRepository
                            .save(new User(request.getUsername(),
                                           passwordEncoder.encode(
                                                   request.getPassword()),
                                           request.getName(),
                                           request.getEmail())
                                          .activate()
                                          .grant(role));

                    profileRepository.save(new Profile(user));

                    return new UserResponse(user);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Role not found",
                                                    List.of("Role has to exists in order to create user.")));
    }

    @Transactional
    public Optional<AccessTokenResponse>
    authenticate(PasswordGrantRequest request) {
        User user = userRepository.findUser(request.getEmail(), null, true)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "User not found",
                                                    List.of("Your email or password is not correct.")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                                   "User not found",
                                   List.of("Your email or password is not correct."));
        }

        return Optional.of(getAccessTokenResponse(user));
    }

    @Transactional
    public Optional<AccessTokenResponse>
    refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findById(request.getRefreshToken())
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Refresh token not found",
                                                    List.of("Refresh token is invalid or expired.")));

        User user = userRepository.findUser(refreshToken.getUserId(), null, true)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "User not found",
                                                    List.of("User does not exist.")));

        refreshTokenRepository.delete(refreshToken); // Prevent from reusing refresh token

        return Optional.of(getAccessTokenResponse(user));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<User>
    listUsers(Boolean active) {
        return userRepository.findUsers(active);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')" +
            " or hasRole('USER') and @identity.isOwner(#userId)")
    public Optional<User>
    getUser(Long userId, Boolean active) {
        return userRepository.findUser(userId, null, active);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')" +
            " or hasAnyRole('STAFF', 'USER') and @identity.isOwner(#userId)")
    public boolean
    deleteUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    profileRepository.deleteByUserId(user.getId());
                    userRepository.delete(user);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Role not found",
                                                      List.of("Role does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')" +
            " or hasAnyRole('STAFF', 'USER') and @identity.isOwner(#userId)")
    public Optional<User>
    inactivateUser(Long userId) {
        User user = userRepository
                .findUser(userId, null, true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist to inactivate.")));

        user.inactivate();

        return Optional.of(userRepository.save(user));
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')" +
            " or hasAnyRole('STAFF', 'USER') and @identity.isOwner(#userId)")
    public Optional<User>
    unregisterUser(Long userId) {
        User user = userRepository
                .findUser(userId, null, true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist to unregister.")));

        user.unregister();

        return Optional.of(userRepository.save(user));
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and @identity.isOwner(#userId)")
    public Optional<User>
    changeUserPassword(Long userId,
                       UserChangePasswordRequest request) {
        User user = userRepository
                .findUser(userId, null, true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User not found to change password.")));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                                   "Password mismatch",
                                   List.of("Your old password is not correct."));
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));

        return Optional.of(userRepository.save(user));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<User>
    resetUserPassword(Long userId,
                      UserResetPasswordRequest request) {
        User user = userRepository
                .findUser(userId, "ROLE_USER", true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist to reset password.")));

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));

        return Optional.of(userRepository.save(user));
    }

    private AccessTokenResponse
    getAccessTokenResponse(User user) {
        // 1. Access token (not saved)
        String accessToken = tokenProvider.createAccessToken(user.getUsername(), user.getId());

        // 2. Refresh token (Redis)
        String refreshToken = tokenProvider.createRefreshToken();
        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId()));

        return new AccessTokenResponse(accessToken,
                                       ACCESS_TOKEN_EXPIRES_IN,
                                       refreshToken);
    }
}
