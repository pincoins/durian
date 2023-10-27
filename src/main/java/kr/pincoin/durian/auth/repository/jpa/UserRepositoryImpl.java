package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.auth.domain.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findUserByEmail(String email, UserStatus status) {
        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(emailEq(email),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUserByUsername(String username, UserStatus status) {
        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(usernameEq(username),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUser(Long id, UserStatus status) {
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
    public List<User> findAdmins(UserStatus status) {
        return getUsers(Role.SYSADMIN, status);
    }

    @Override
    public List<User> findStaffs(UserStatus status) {
        return getUsers(Role.STAFF, status);
    }

    private Optional<User> getUser(Long id, Role role, UserStatus status) {
        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(idEq(id),
                       roleEq(role),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    private List<User> getUsers(Role role, UserStatus status) {
        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .where(roleEq(role),
                       statusEq(status));

        return contentQuery.fetch();
    }

    BooleanExpression usernameEq(String username) {
        return username != null ? user.username.eq(username) : null;
    }

    BooleanExpression emailEq(String email) {
        return email != null ? user.email.eq(email) : null;
    }

    BooleanExpression idEq(Long id) {
        return id != null ? user.id.eq(id) : null;
    }

    BooleanExpression statusEq(UserStatus status) {
        return status != null ? user.status.eq(status) : user.status.eq(UserStatus.NORMAL);
    }

    BooleanExpression roleEq(Role role) {
        return role != null ? user.role.eq(role) : user.role.eq(Role.MEMBER);
    }
}
