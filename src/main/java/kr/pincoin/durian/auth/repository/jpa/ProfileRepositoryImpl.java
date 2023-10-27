package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.QProfile;
import kr.pincoin.durian.auth.domain.QUser;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.auth.domain.QProfile.profile;
import static kr.pincoin.durian.auth.domain.QUser.user;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Profile> findMember(Long id, UserStatus status) {
        JPAQuery<Profile> contentQuery = queryFactory
                .select(profile)
                .from(profile)
                .innerJoin(profile.user, user)
                .fetchJoin()
                .where(idEq(id),
                       roleEq(),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<Profile> findMember(Long id, List<UserStatus> statuses) {
        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        JPAQuery<Profile> contentQuery = queryFactory
                .select(profile)
                .from(profile)
                .innerJoin(profile.user, user)
                .fetchJoin()
                .where(idEq(id),
                       roleEq(),
                       statusIn(statuses));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<Profile> findMembers(UserStatus status) {
        JPAQuery<Profile> contentQuery = queryFactory
                .select(profile)
                .from(profile)
                .innerJoin(profile.user, user)
                .fetchJoin()
                .where(roleEq(),
                       statusEq(status));

        return contentQuery.fetch();
    }

    BooleanExpression idEq(Long id) {
        return id != null ? user.id.eq(id) : null;
    }

    BooleanExpression roleEq() {
        return user.role.eq(Role.MEMBER);
    }

    BooleanExpression statusEq(UserStatus status) {
        return status != null ? user.status.eq(status) : user.status.eq(UserStatus.NORMAL);
    }

    BooleanExpression statusIn(List<UserStatus> statuses) {
        return statuses != null ? user.status.in(statuses) : user.status.eq(UserStatus.NORMAL);
    }
}
