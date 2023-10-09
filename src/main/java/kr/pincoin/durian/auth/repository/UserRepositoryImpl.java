package kr.pincoin.durian.auth.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.QRole;
import kr.pincoin.durian.auth.domain.QUser;
import kr.pincoin.durian.auth.domain.User;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findUser(String email, Boolean active) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(emailEq(email),
                       activeEq(active));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<User> findUser(Long id, Boolean active) {
        QUser user = QUser.user;
        QRole role = QRole.role;

        JPAQuery<User> contentQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.role, role)
                .fetchJoin()
                .where(idEq(id),
                       activeEq(active));

        return Optional.ofNullable(contentQuery.fetchOne());
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
}
