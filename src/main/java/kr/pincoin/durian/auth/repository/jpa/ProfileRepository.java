package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE Profile WHERE user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
