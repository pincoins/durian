package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findUser(String email, String roleCode, Boolean active);

    Optional<User> findUser(Long id, String roleCode, Boolean active);

    List<User> findUsers(Boolean active);
}
