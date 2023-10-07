package kr.pincoin.durian.auth.repository;

import kr.pincoin.durian.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long>, UserRepositoryQuery {
}
