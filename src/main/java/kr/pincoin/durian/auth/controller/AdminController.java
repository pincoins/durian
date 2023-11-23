package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.controller.dto.*;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.service.AdminService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @GetMapping("")
    public ResponseEntity<List<UserResponse>>
    adminList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(adminService.listAdmins(status)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    adminDetail(@PathVariable Long userId,
                @RequestParam(name = "status", required = false) UserStatus status) {
        return adminService.getAdmin(userId, status)
                .map(admin -> ResponseEntity.ok().body(new UserResponse(admin)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Admin not found",
                                                    List.of("Admin does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse>
    adminCreate(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = new UserResponse(adminService.createAdmin(request));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void>
    adminDelete(@PathVariable Long userId) {
        if (adminService.deleteAdmin(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{userId}/change-username")
    public ResponseEntity<UserResponse>
    adminChangeUsername(@PathVariable Long userId,
                        @Valid @RequestBody UserChangeUsernameRequest request) {
        return adminService.changeUsername(userId, request)
                .map(admin -> ResponseEntity.ok().body(new UserResponse(admin)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Admin not found",
                                                    List.of("Failed to change admin username.")));
    }

    @PutMapping("/{userId}/change-full-name")
    public ResponseEntity<UserResponse>
    adminChangeFullName(@PathVariable Long userId,
                        @Valid @RequestBody UserChangeFullNameRequest request) {
        return adminService.changeFullName(userId, request)
                .map(admin -> ResponseEntity.ok().body(new UserResponse(admin)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Admin not found",
                                                    List.of("Failed to change admin full name.")));
    }
}
