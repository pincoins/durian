package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.RefreshToken;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.dto.*;
import kr.pincoin.durian.auth.jwt.TokenProvider;
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

    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       TokenProvider tokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse
    createUser(UserCreateRequest request) {
        User user = userRepository.save(new User(request.getUsername(),
                                                 passwordEncoder.encode(request.getPassword()),
                                                 request.getName(),
                                                 request.getEmail()).activate());
        return new UserResponse(user);
    }

    @Transactional
    public Optional<AccessTokenResponse>
    authenticate(PasswordGrantRequest request) {
        User user = userRepository.findUser(request.getEmail(), true)
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

        User user = userRepository.findUser(refreshToken.getUserId(), true)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "User not found",
                                                    List.of("User does not exist.")));

        return Optional.of(getAccessTokenResponse(user));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<User> listUsers(Boolean active) {
        return userRepository.findUsers(active);
    }

    private AccessTokenResponse getAccessTokenResponse(User user) {
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
