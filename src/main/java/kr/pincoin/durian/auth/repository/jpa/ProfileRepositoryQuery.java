package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;

import java.util.List;
import java.util.Optional;

public interface ProfileRepositoryQuery {
    Optional<Profile> findMember(Long id, UserStatus status);

    Optional<Profile> findMember(Long id, List<UserStatus> statuses);

    List<Profile> findMembers(UserStatus status);
}
