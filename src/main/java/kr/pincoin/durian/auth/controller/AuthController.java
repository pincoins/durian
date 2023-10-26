package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.dto.AccessTokenResponse;
import kr.pincoin.durian.auth.dto.PasswordGrantRequest;
import kr.pincoin.durian.auth.dto.RefreshTokenRequest;
import kr.pincoin.durian.auth.dto.UserChangePasswordRequest;
import kr.pincoin.durian.auth.service.AuthService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AccessTokenResponse>
    authenticate(@Valid @RequestBody PasswordGrantRequest request) {
        return authService.authenticate(request)
                .map(response -> {
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add("Authorization", "Bearer " + response.getAccessToken());
                    return ResponseEntity.ok().headers(responseHeaders).body(response);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                                                    "Login failure",
                                                    List.of("Username, email or password is not correct.")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse>
    refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request)
                .map(response -> {
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add("Authorization", "Bearer " + response.getAccessToken());
                    return ResponseEntity.ok().headers(responseHeaders).body(response);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                                                    "Invalid refresh token",
                                                    List.of("Your refresh token is invalid or expired.")));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean>
    userPasswordChange(@Valid @RequestBody UserChangePasswordRequest request) {
        log.warn("{} {} {}", request.getUserId(), request.getNewPassword(), request.getOldPassword());
        boolean result = authService.changePassword(request.getUserId(), request);
        return ResponseEntity.ok().body(result);
    }
}
