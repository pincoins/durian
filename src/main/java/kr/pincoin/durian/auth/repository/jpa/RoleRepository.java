package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE User" +
            " SET role = null, status = kr.pincoin.durian.auth.domain.converter.UserStatus.INACTIVE" +
            " WHERE role.id = :roleId")
    void revokeUsers(@Param("roleId") Long roleId);
}
