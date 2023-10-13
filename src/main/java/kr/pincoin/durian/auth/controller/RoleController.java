package kr.pincoin.durian.auth.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.dto.RoleCreateRequest;
import kr.pincoin.durian.auth.dto.RoleResponse;
import kr.pincoin.durian.auth.service.RoleService;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin("*")
@Slf4j
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<List<RoleResponse>>
    roleList() {
        return ResponseEntity.ok()
                .body(roleService.listRoles()
                              .stream()
                              .map(role -> new RoleResponse(
                                      role.getId(),
                                      role.getCode(),
                                      role.getName()))
                              .toList());
    }

    @PostMapping("")
    public ResponseEntity<RoleResponse>
    roleCreate(@Valid @RequestBody RoleCreateRequest request) {
        return roleService.createRole(request)
                .map(role -> ResponseEntity.ok().body(new RoleResponse(role)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Role creation failure",
                                                    List.of("Failed to create role")));
    }

    @DeleteMapping("{roleId}")
    public ResponseEntity<Object>
    roleDelete(@PathVariable Long roleId) {
        if (roleService.deleteRole(roleId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
