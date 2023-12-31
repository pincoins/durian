package kr.pincoin.durian.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.pincoin.durian.auth.controller.dto.*;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.service.DanalService;
import kr.pincoin.durian.auth.service.MemberService;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.common.service.AwsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    private final AwsService awsService;

    private final DanalService danalService;

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
    memberCreate(@Valid @RequestBody UserCreateRequest request, HttpServletRequest servletRequest) {
        ProfileResponse response = new ProfileResponse(memberService.createMember(request, servletRequest));
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
    public ResponseEntity<Void>
    memberDelete(@PathVariable Long userId) {
        if (memberService.deleteMember(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
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

    @PutMapping("/{userId}/change-username")
    public ResponseEntity<ProfileResponse>
    memberChangeUsername(@PathVariable Long userId,
                         @Valid @RequestBody UserChangeUsernameRequest request) {
        return memberService.changeUsername(userId, request)
                .map(member -> ResponseEntity.ok().body(new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to change member username.")));
    }

    @PutMapping("/{userId}/change-full-name")
    public ResponseEntity<ProfileResponse>
    memberChangeFullName(@PathVariable Long userId,
                         @Valid @RequestBody UserChangeFullNameRequest request) {
        return memberService.changeFullName(userId, request)
                .map(member -> ResponseEntity.ok().body(new ProfileResponse(member)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to change member full name.")));
    }

    @PostMapping("/send-email-verification")
    public ResponseEntity<EmailVerificationResponse>
    memberSendEmailVerification(@Valid @RequestBody EmailVerificationRequest request,
                                HttpServletRequest servletRequest) {
        return ResponseEntity.ok()
                .body(new EmailVerificationResponse(memberService.sendVerificationEmail(request, servletRequest)));
    }

    @PostMapping("/send-email-code")
    public ResponseEntity<EmailVerificationResponse>
    memberSendEmailCode(@Valid @RequestBody EmailCodeRequest request,
                        HttpServletRequest servletRequest) {
        return ResponseEntity.ok()
                .body(new EmailVerificationResponse(memberService.sendEmailCode(request, servletRequest)));
    }

    // request
    // verify phone
    // reject phone verification
    // revoke phone verification

    // request
    // verify document
    // reject document verification
    // revoke document verification

    @PostMapping("/{userId}/upload/photo-id")
    public ResponseEntity<String>
    memberUploadPhotoId(@PathVariable Long userId,
                        @Valid @RequestPart(value = "file") MultipartFile file) {
        String s3file = awsService.uploadFile("shop", file);
        log.debug(s3file);
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/{userId}/upload/credit-card")
    public ResponseEntity<String>
    memberUploadCreditCard(@PathVariable Long userId,
                           @Valid @RequestPart(value = "file") MultipartFile file) {
        String s3file = awsService.uploadFile("shop", file);
        log.debug(s3file);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{userId}/danal")
    public ResponseEntity<String>
    memberRequestPhoneVerification(@PathVariable Long userId) {
        return danalService.callItemSend(userId)
                .map(tid -> ResponseEntity.ok().body(tid))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Danal server error",
                                                    List.of("Failed to call itemsend command.")));
    }

    @PostMapping("/{userId}/danal")
    public ResponseEntity<String>
    memberVerifyPhone(@PathVariable Long userId) {
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<Favorites>
    memberFetchFavorites(@PathVariable Long userId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;

        if (user == null) {
            return ResponseEntity.ok().body(Favorites.builder().build());
        }

        return memberService.getMember(userId, UserStatus.NORMAL)
                .map(result -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return ResponseEntity.ok().body(mapper.readValue(result.getFavorites(), Favorites.class));
                    } catch (JsonProcessingException ignored) {
                        throw new ApiException(HttpStatus.CONFLICT,
                                               "Favorites JSON parse error",
                                               List.of("Favorites json format is invalid."));
                    }
                })
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Member does not exist to retrieve.")));
    }

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<Favorites>
    memberReplaceFavorites(@PathVariable Long userId,
                           @Valid @RequestBody Favorites request) {
        return memberService.replaceFavorites(userId, request)
                .map(member -> ResponseEntity.ok().body(request))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Member not found",
                                                    List.of("Failed to update favorites.")));
    }
}
