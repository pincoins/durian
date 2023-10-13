package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Role;

import java.util.Optional;

public interface RoleRepositoryQuery {
    Optional<Role> findRole(String code);
}
