package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserChangePasswordRequest;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.service.UserService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>>
    userList(@RequestParam(name = "roleCode", required = false) String roleCode,
             @RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(userService.listUsers(roleCode, status)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    userDetail(@PathVariable Long userId,
               @RequestParam(name = "roleCode", required = false) String roleCode,
               @RequestParam(name = "status", required = false) UserStatus status) {
        return userService.getUser(userId, roleCode, status)
                .map(user -> ResponseEntity.ok().body(new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist to retrieve.")));
    }

    @PutMapping("{userId}/inactivate")
    public ResponseEntity<UserResponse>
    userInactivate(@PathVariable Long userId) {
        return userService.inactivateUser(userId)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to inactivate user.")));
    }

    @PutMapping("{userId}/unregister")
    public ResponseEntity<UserResponse>
    userUnregister(@PathVariable Long userId) {
        return userService.unregisterUser(userId)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to inactivate user.")));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    userDelete(@PathVariable Long userId) {
        if (userService.deleteUser(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<UserResponse>
    userPasswordChange(@PathVariable Long userId,
                       @Valid @RequestBody UserChangePasswordRequest request) {
        return userService.changeUserPassword(userId, request)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to change user password.")));
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse>
    userPasswordReset(@PathVariable Long userId,
                      @Valid @RequestBody UserResetPasswordRequest request) {
        return userService.resetUserPassword(userId, request)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to reset user password.")));
    }
}
