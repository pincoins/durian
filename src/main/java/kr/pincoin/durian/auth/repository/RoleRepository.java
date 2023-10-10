package kr.pincoin.durian.auth.repository;

import kr.pincoin.durian.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
