package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserProfileResponse;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserProfileResult;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
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
    public ResponseEntity<List<UserProfileResponse>>
    memberList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(memberService.listMembers(status)
                              .stream()
                              .map(UserProfileResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse>
    memberDetail(@PathVariable Long userId,
                 @RequestParam(name = "status", required = false) UserStatus status) {
        return memberService.getMember(userId, status)
                .map(result -> ResponseEntity.ok().body(new UserProfileResponse(result)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<UserProfileResponse>
    memberCreate(@Valid @RequestBody UserCreateRequest request) {
        UserProfileResult result = memberService.createMember(request);
        return ResponseEntity.ok().body(new UserProfileResponse(result));
    }

    @PutMapping("{userId}/approve")
    public ResponseEntity<UserProfileResponse>
    memberApprove(@PathVariable Long userId) {
        return memberService.approveMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new UserProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to approve member.")));
    }

    @PutMapping("{userId}/inactivate")
    public ResponseEntity<UserProfileResponse>
    memberInactivate(@PathVariable Long userId) {
        return memberService.inactivateMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new UserProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to inactivate member.")));
    }

    @PutMapping("{userId}/activate")
    public ResponseEntity<UserProfileResponse>
    memberActivate(@PathVariable Long userId) {
        return memberService.activateMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new UserProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to activate member.")));
    }

    @PutMapping("{userId}/unregister")
    public ResponseEntity<UserProfileResponse>
    memberUnregister(@PathVariable Long userId) {
        return memberService.unregisterMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new UserProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to inactivate member.")));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    memberDelete(@PathVariable Long userId) {
        if (memberService.deleteMember(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<UserProfileResponse>
    memberPasswordReset(@PathVariable Long userId,
                        @Valid @RequestBody UserResetPasswordRequest request) {
        return memberService.resetMemberPassword(userId, request)
                .map(member -> ResponseEntity.ok().body(
                        new UserProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to reset member password.")));
    }
}
