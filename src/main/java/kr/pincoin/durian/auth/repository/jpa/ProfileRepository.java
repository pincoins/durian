package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositoryQuery {
}
