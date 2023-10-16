package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.service.StaffService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@CrossOrigin("*")
@Slf4j
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }


    @GetMapping("")
    public ResponseEntity<List<UserResponse>>
    adminList(@RequestParam(name = "status", required = false) UserStatus status) {
        return ResponseEntity.ok()
                .body(staffService.listStaffs(status)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    adminDetail(@PathVariable Long userId,
                @RequestParam(name = "status", required = false) UserStatus status) {
        return staffService.getStaff(userId, status)
                .map(staff -> ResponseEntity.ok().body(new UserResponse(staff)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Staff not found",
                                                    List.of("Staff does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse>
    adminCreate(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = staffService.createStaff(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    adminDelete(@PathVariable Long userId) {
        if (staffService.deleteStaff(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
