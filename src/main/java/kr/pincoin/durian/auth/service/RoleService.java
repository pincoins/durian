package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.Role;
import kr.pincoin.durian.auth.repository.jpa.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('SYSADMIN')")
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
