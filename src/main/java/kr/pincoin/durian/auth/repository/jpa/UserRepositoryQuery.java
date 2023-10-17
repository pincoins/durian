package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findUser(String email, String roleCode, UserStatus status);

    Optional<User> findUser(Long id, UserStatus status);

    Optional<User> findAdmin(Long id, UserStatus status);

    Optional<User> findStaff(Long id, UserStatus status);

    Optional<User> findMember(Long id, UserStatus status);

    List<User> findAdmins(UserStatus status);

    List<User> findStaffs(UserStatus status);

    List<User> findMembers(UserStatus status);
}
