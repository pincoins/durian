package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.QProfile;
import kr.pincoin.durian.auth.domain.QUser;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.dto.UserProfileResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findUser(String email, UserStatus status) {
        QUser user = QUser.user;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(emailEq(email),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUser(Long id, UserStatus status) {
        QUser user = QUser.user;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(idEq(id),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findAdmin(Long id, UserStatus status) {
        return getUser(id, Role.SYSADMIN, status);
    }

    @Override
    public Optional<User> findStaff(Long id, UserStatus status) {
        return getUser(id, Role.STAFF, status);
    }

    @Override
    public Optional<UserProfileResult> findMember(Long id, UserStatus status) {
        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        JPAQuery<UserProfileResult> contentQuery = queryFactory
                .select(Projections.fields(UserProfileResult.class,
                                           user.as("user"),
                                           profile.as("profile")))
                .from(user)
                .innerJoin(profile)
                .on(profile.user.id.eq(user.id))
                .where(idEq(id),
                       roleEq(Role.MEMBER),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<UserProfileResult> findMember(Long id, List<UserStatus> statuses) {
        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        JPAQuery<UserProfileResult> contentQuery = queryFactory
                .select(Projections.fields(UserProfileResult.class,
                                           user.as("user"),
                                           profile.as("profile")))
                .from(user)
                .innerJoin(profile)
                .on(profile.user.id.eq(user.id))
                .where(idEq(id),
                       roleEq(Role.MEMBER),
                       statusIn(statuses));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<User> findAdmins(UserStatus status) {
        return getUsers(Role.SYSADMIN, status);
    }

    @Override
    public List<User> findStaffs(UserStatus status) {
        return getUsers(Role.STAFF, status);
    }

    @Override
    public List<UserProfileResult> findMembers(UserStatus status) {
        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        JPAQuery<UserProfileResult> contentQuery = queryFactory
                .select(Projections.fields(UserProfileResult.class,
                                           user.as("user"),
                                           profile.as("profile")))
                .from(user)
                .innerJoin(profile)
                .on(profile.user.id.eq(user.id))
                .where(roleEq(Role.MEMBER),
                       statusEq(status));

        return contentQuery.fetch();
    }

    private Optional<User> getUser(Long id, Role role, UserStatus status) {
        QUser user = QUser.user;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(idEq(id),
                       roleEq(role),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    private List<User> getUsers(Role role, UserStatus status) {
        QUser user = QUser.user;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(roleEq(role),
                       statusEq(status));

        return contentQuery.fetch();
    }

    BooleanExpression usernameEq(String username) {
        QUser user = QUser.user;

        return username != null ? user.username.eq(username) : null;
    }

    BooleanExpression emailEq(String email) {
        QUser user = QUser.user;

        return email != null ? user.email.eq(email) : null;
    }

    BooleanExpression idEq(Long id) {
        QUser user = QUser.user;

        return id != null ? user.id.eq(id) : null;
    }

    BooleanExpression statusEq(UserStatus status) {
        QUser user = QUser.user;

        return status != null ? user.status.eq(status) : user.status.eq(UserStatus.NORMAL);
    }

    BooleanExpression statusIn(List<UserStatus> statuses) {
        QUser user = QUser.user;

        return statuses != null ? user.status.in(statuses) : user.status.eq(UserStatus.NORMAL);
    }

    BooleanExpression roleEq(Role role) {
        QUser user = QUser.user;

        return role != null ? user.role.eq(role) : user.role.eq(Role.MEMBER);
    }
}
