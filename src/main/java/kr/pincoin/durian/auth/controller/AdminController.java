package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.service.AdminService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@CrossOrigin("*")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

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
        UserResponse response = adminService.createAdmin(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    adminDelete(@PathVariable Long userId) {
        if (adminService.deleteAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
