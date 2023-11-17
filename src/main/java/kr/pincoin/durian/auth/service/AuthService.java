package kr.pincoin.durian.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.controller.dto.AccessTokenResponse;
import kr.pincoin.durian.auth.controller.dto.PasswordGrantRequest;
import kr.pincoin.durian.auth.controller.dto.RefreshTokenRequest;
import kr.pincoin.durian.auth.controller.dto.UserChangePasswordRequest;
import kr.pincoin.durian.auth.domain.RefreshToken;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import kr.pincoin.durian.auth.repository.redis.RefreshTokenRepository;
import kr.pincoin.durian.auth.util.jwt.TokenProvider;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.common.util.RequestHeaderParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthService {
    @Value("${auth.jwt.access-token-expires-in}")
    private int jwtAccessTokenExpiresIn;

    @Value("${auth.jwt.refresh-token-expires-in}")
    private int jwtRefreshTokenExpiresIn;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final RequestHeaderParser requestHeaderParser;

    @Transactional
    public Optional<AccessTokenResponse>
    authenticate(PasswordGrantRequest request,
                 HttpServletRequest servletRequest) {
        User user = userRepository.findUserByEmail(request.getEmail(), UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Authentication failed",
                                                    List.of("Your email or password is not correct.")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                                   "Authentication failed",
                                   List.of("Your email or password is not correct."));
        }

        return Optional.of(getAccessTokenResponse(user, servletRequest));
    }

    @Transactional
    public Optional<AccessTokenResponse>
    refresh(RefreshTokenRequest request,
            String refreshToken,
            HttpServletRequest servletRequest) {
        RefreshToken refreshTokenFound = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "Refresh token not found",
                                                    List.of("Refresh token is invalid or expired.")));

        if (!refreshTokenFound.getIpAddress().equals(requestHeaderParser.changeHttpServletRequest(servletRequest).getIpAddress())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                                   "Refresh token IP address mismatch",
                                   List.of("Your IP address was changed after token issued."));
        }

        User user = userRepository.findUser(refreshTokenFound.getUserId(), UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,
                                                    "User not found",
                                                    List.of("User does not exist.")));

        // Refresh Token Rotation(RTR) - Prevent from reusing refresh token
        refreshTokenRepository.delete(refreshTokenFound);

        return Optional.of(getAccessTokenResponse(user, servletRequest));
    }

    public void
    deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findById(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and @identity.isOwner(#userId)")
    public boolean
    changePassword(Long userId,
                   UserChangePasswordRequest request) {
        User user = userRepository
                .findUser(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User not found to change password.")));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                                   "Password mismatch",
                                   List.of("Your old password is not correct."));
        }

        return user.changePassword(passwordEncoder.encode(request.getNewPassword())) != null;
    }

    private AccessTokenResponse
    getAccessTokenResponse(User user, HttpServletRequest servletRequest) {
        // 1. Access token (not saved)
        String accessToken = tokenProvider.createAccessToken(user);

        // 2. Refresh token (Redis)
        String refreshToken = tokenProvider.createRefreshToken();

        String ipAddress = requestHeaderParser.changeHttpServletRequest(servletRequest).getIpAddress();

        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId(), ipAddress)
                                            .setTimeout((long) jwtRefreshTokenExpiresIn));

        return new AccessTokenResponse(accessToken,
                                       jwtAccessTokenExpiresIn,
                                       refreshToken);
    }
}
