package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.RefreshToken;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.dto.AccessTokenResponse;
import kr.pincoin.durian.auth.dto.PasswordGrantRequest;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.jwt.TokenProvider;
import kr.pincoin.durian.auth.repository.RefreshTokenRepository;
import kr.pincoin.durian.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.findUser(request.getEmail(), true)
                .map(user -> {
                    AccessTokenResponse response = null;

                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        response = getAccessTokenResponse(user);
                    }

                    return Optional.ofNullable(response);
                })
                .orElseGet(Optional::empty);
    }

    private AccessTokenResponse getAccessTokenResponse(User user) {
        // 1. Access token (nowhere)
        String accessToken = tokenProvider.createAccessToken(user.getUsername(), user.getId());

        // 2. Refresh token (Redis)
        String refreshToken = tokenProvider.createRefreshToken();
        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId()));

        return new AccessTokenResponse(accessToken,
                                       ACCESS_TOKEN_EXPIRES_IN,
                                       refreshToken);
    }
}
