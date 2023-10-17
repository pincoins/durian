package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository  extends JpaRepository<User, Long>, UserRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE User WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}