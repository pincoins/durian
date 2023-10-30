package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.ProfileResponse;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResetPasswordRequest;
import kr.pincoin.durian.auth.service.MemberService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<ProfileResponse>>
    memberList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(memberService.listMembers(status)
                              .stream()
                              .map(ProfileResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse>
    memberDetail(@PathVariable Long userId,
                 @RequestParam(name = "status", required = false) UserStatus status) {
        return memberService.getMember(userId, status)
                .map(result -> ResponseEntity.ok().body(new ProfileResponse(result)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<ProfileResponse>
    memberCreate(@Valid @RequestBody UserCreateRequest request) {
        ProfileResponse response = new ProfileResponse(memberService.createMember(request));
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("{userId}/approve")
    public ResponseEntity<ProfileResponse>
    memberApprove(@PathVariable Long userId) {
        return memberService.approveMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to approve member.")));
    }

    @PutMapping("{userId}/inactivate")
    public ResponseEntity<ProfileResponse>
    memberInactivate(@PathVariable Long userId) {
        return memberService.inactivateMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to inactivate member.")));
    }

    @PutMapping("{userId}/activate")
    public ResponseEntity<ProfileResponse>
    memberActivate(@PathVariable Long userId) {
        return memberService.activateMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to activate member.")));
    }

    @PutMapping("{userId}/unregister")
    public ResponseEntity<ProfileResponse>
    memberUnregister(@PathVariable Long userId) {
        return memberService.unregisterMember(userId)
                .map(member -> ResponseEntity.ok().body(
                        new ProfileResponse(member)))
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
    public ResponseEntity<ProfileResponse>
    memberPasswordReset(@PathVariable Long userId,
                        @Valid @RequestBody UserResetPasswordRequest request) {
        return memberService.resetMemberPassword(userId, request)
                .map(member -> ResponseEntity.ok().body(
                        new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to reset member password.")));
    }
}
