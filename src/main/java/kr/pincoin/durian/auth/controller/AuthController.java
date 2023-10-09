package kr.pincoin.durian.auth.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Slf4j
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse>
    createUser(@Valid @RequestBody UserCreateRequest request) throws ConstraintViolationException,
                                                                     DataIntegrityViolationException {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok().body(response);
    }
}
