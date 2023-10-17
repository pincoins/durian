package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.QRole;
import kr.pincoin.durian.auth.domain.QUser;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findUser(String email, String roleCode, UserStatus status) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(emailEq(email),
                       roleCodeEq(roleCode),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUser(Long id, UserStatus status) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(idEq(id),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findAdmin(Long id, UserStatus status) {
        return getUser(id, "ROLE_SYSADMIN", status);
    }

    @Override
    public Optional<User> findStaff(Long id, UserStatus status) {
        return getUser(id, "ROLE_STAFF", status);
    }

    @Override
    public Optional<User> findMember(Long id, UserStatus status) {
        return getUser(id, "ROLE_MEMBER", status);
    }


    @Override
    public List<User> findAdmins(UserStatus status) {
        return getUsers("ROLE_SYSADMIN", status);
    }

    @Override
    public List<User> findStaffs(UserStatus status) {
        return getUsers("ROLE_STAFF", status);
    }

    @Override
    public List<User> findMembers(UserStatus status) {
        return getUsers("ROLE_MEMBER", status);
    }

    private Optional<User> getUser(Long id, String roleCode, UserStatus status) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(idEq(id),
                       roleCodeEq(roleCode),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    private List<User> getUsers(String roleCode, UserStatus status) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(roleCodeEq(roleCode),
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

    BooleanExpression roleCodeEq(String roleCode) {
        QRole role = QRole.role;

        return roleCode != null ? role.code.eq(roleCode) : role.code.eq("ROLE_USER");
    }
}
