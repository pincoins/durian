package kr.pincoin.durian.auth.repository;

import kr.pincoin.durian.auth.domain.User;

import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findUser(String username, Boolean active);

    Optional<User> findUser(Long id, Boolean active);
}
