package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.QRole;
import kr.pincoin.durian.auth.domain.QUser;
import kr.pincoin.durian.auth.domain.User;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findUser(String email, String roleCode, Boolean active) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(emailEq(email),
                       activeEq(active),
                       roleCodeEq(roleCode));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUser(Long id, String roleCode, Boolean active) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(idEq(id),
                       activeEq(active),
                       roleCodeEq(roleCode));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<User> findUsers(Boolean active) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(activeEq(active));

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

    BooleanExpression activeEq(Boolean active) {
        QUser user = QUser.user;

        return active != null ? user.active.eq(active) : user.active.eq(true);
    }

    BooleanExpression roleCodeEq(String roleCode) {
        QRole role = QRole.role;

        return roleCode != null ? role.code.eq(roleCode) : null;
    }
}
