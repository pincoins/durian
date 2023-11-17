package kr.pincoin.durian.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.pincoin.durian.auth.controller.dto.AccessTokenResponse;
import kr.pincoin.durian.auth.controller.dto.PasswordGrantRequest;
import kr.pincoin.durian.auth.controller.dto.RefreshTokenRequest;
import kr.pincoin.durian.auth.controller.dto.UserChangePasswordRequest;
import kr.pincoin.durian.auth.service.AuthService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Value("${auth.jwt.refresh-token-expires-in}")
    private int jwtRefreshTokenExpiresIn;

    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AccessTokenResponse>
    authenticate(@Valid @RequestBody PasswordGrantRequest request,
                 HttpServletRequest servletRequest) {
        return authService.authenticate(request, servletRequest)
                .map(response -> {
                    HttpHeaders responseHeaders = getHttpHeaders(response);

                    return ResponseEntity.ok().headers(responseHeaders).body(response);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                                                    "Login failure",
                                                    List.of("Username, email or password is not correct.")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse>
    refresh(@Valid @RequestBody RefreshTokenRequest request,
            @CookieValue("refreshToken") String refreshToken,
            HttpServletRequest servletRequest) {
        return authService.refresh(request, refreshToken, servletRequest)
                .map(response -> {
                    HttpHeaders responseHeaders = getHttpHeaders(response);

                    return ResponseEntity.ok().headers(responseHeaders).body(response);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                                                    "Invalid refresh token",
                                                    List.of("Your refresh token is invalid or expired.")));
    }

    @DeleteMapping("/refresh")
    public ResponseEntity<Void>
    refreshTokenDelete(@CookieValue("refreshToken") String refreshToken) {
        authService.deleteRefreshToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "expired")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(0)
                .path("/").build();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().headers(responseHeaders).build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean>
    userPasswordChange(@Valid @RequestBody UserChangePasswordRequest request) {
        boolean result = authService.changePassword(request.getUserId(), request);
        return ResponseEntity.ok().body(result);
    }

    private HttpHeaders
    getHttpHeaders(AccessTokenResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization", "Bearer " + response.getAccessToken());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(jwtRefreshTokenExpiresIn)
                .path("/").build();

        responseHeaders.add("Set-Cookie", cookie.toString());
        return responseHeaders;
    }
}
