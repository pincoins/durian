package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.User;

import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findUser(String email, Boolean active);

    Optional<User> findUser(Long id, Boolean active);
}
