package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.service.MemberService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin("*")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>>
    userList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(memberService.listMembers(status)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    userDetail(@PathVariable Long userId,
               @RequestParam(name = "status", required = false) UserStatus status) {
        return memberService.getMember(userId, status)
                .map(user -> ResponseEntity.ok().body(new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse>
    userCreate(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = memberService.createUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("{userId}/inactivate")
    public ResponseEntity<UserResponse>
    userInactivate(@PathVariable Long userId) {
        return memberService.inactivateMember(userId)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to inactivate user.")));
    }

    @PutMapping("{userId}/unregister")
    public ResponseEntity<UserResponse>
    userUnregister(@PathVariable Long userId) {
        return memberService.unregisterMember(userId)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to inactivate user.")));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    userDelete(@PathVariable Long userId) {
        if (memberService.deleteMember(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse>
    userPasswordReset(@PathVariable Long userId,
                      @Valid @RequestBody UserResetPasswordRequest request) {
        return memberService.resetMemberPassword(userId, request)
                .map(user -> ResponseEntity.ok().body(
                        new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("Failed to reset user password.")));
    }
}
