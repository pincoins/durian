package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.Role;
import kr.pincoin.durian.auth.dto.RoleCreateRequest;
import kr.pincoin.durian.auth.repository.jpa.RoleRepository;
import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public List<Role>
    listRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public Optional<Role>
    createRole(RoleCreateRequest request) {
        Role role = roleRepository.save(new Role(request.getCode(), request.getName()));
        return Optional.of(role);
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public boolean
    deleteRole(Long roleId) {
        return roleRepository.findById(roleId)
                .map(role -> {
                    roleRepository.revokeUsers(role.getId());
                    roleRepository.delete(role);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Role not found",
                                                      List.of("Role does not exist to delete.")));
    }
}
