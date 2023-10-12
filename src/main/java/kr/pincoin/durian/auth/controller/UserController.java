package kr.pincoin.durian.auth.controller;

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
    userList(@RequestParam(name = "active", required = false) Boolean active) {
        return ResponseEntity.ok()
                .body(userService.listUsers(active)
                              .stream()
                              .map(UserResponse::new)
                              .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>
    userDetail(@PathVariable Long userId,
               @RequestParam(name = "active", required = false) Boolean active) {
        return userService.getUser(userId, active)
                .map(user -> ResponseEntity.ok().body(new UserResponse(user)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "User not found",
                                                    List.of("User does not exist.")));
    }

    // changePassword
}
