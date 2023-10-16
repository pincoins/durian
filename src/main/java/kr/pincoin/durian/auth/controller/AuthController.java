package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.dto.*;
import kr.pincoin.durian.auth.service.AuthService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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
    public ResponseEntity<UserResponse>
    userPasswordChange(@Valid @RequestBody UserChangePasswordRequest request) {
        return authService.changePassword(request.getUserId(), request)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to change user password.")));
    }
}
