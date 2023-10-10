package kr.pincoin.durian.auth.controller;

import kr.pincoin.durian.auth.dto.RoleResponse;
import kr.pincoin.durian.auth.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
