package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findUser(String email, String roleCode, UserStatus status);

    Optional<User> findUser(Long id, String roleCode, UserStatus status);

    List<User> findUsers(String roleCode, UserStatus status);
}
