package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.controller.dto.*;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.service.StaffService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class StaffController {
    private final StaffService staffService;

    @GetMapping("")
    public ResponseEntity<List<UserResponse>>
    staffList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(staffService.listStaffs(status)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    staffDetail(@PathVariable Long userId,
                @RequestParam(name = "status", required = false) UserStatus status) {
        return staffService.getStaff(userId, status)
                .map(staff -> ResponseEntity.ok().body(new UserResponse(staff)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Staff not found",
                                                    List.of("Staff does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse>
    staffCreate(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = new UserResponse(staffService.createStaff(request));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void>
    staffDelete(@PathVariable Long userId) {
        if (staffService.deleteStaff(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse>
    staffPasswordReset(@PathVariable Long userId,
                       @Valid @RequestBody UserResetPasswordRequest request) {
        return staffService.resetStaffPassword(userId, request)
                .map(staff -> ResponseEntity.ok().body(
                        new UserResponse(staff)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Staff not found",
                                                    List.of("Failed to reset staff password.")));
    }


    @PutMapping("/{userId}/change-username")
    public ResponseEntity<UserResponse>
    adminChangeUsername(@PathVariable Long userId,
                        @Valid @RequestBody UserChangeUsernameRequest request) {
        return staffService.changeUsername(userId, request)
                .map(staff -> ResponseEntity.ok().body(new UserResponse(staff)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Staff not found",
                                                    List.of("Failed to change staff username.")));
    }

    @PutMapping("/{userId}/change-full-name")
    public ResponseEntity<UserResponse>
    adminChangeFullName(@PathVariable Long userId,
                        @Valid @RequestBody UserChangeFullNameRequest request) {
        return staffService.changeFullName(userId, request)
                .map(staff -> ResponseEntity.ok().body(new UserResponse(staff)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Staff not found",
                                                    List.of("Failed to change staff full name.")));
    }
}
