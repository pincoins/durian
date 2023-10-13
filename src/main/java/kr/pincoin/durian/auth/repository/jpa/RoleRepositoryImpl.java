package kr.pincoin.durian.auth.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.auth.domain.QRole;
import kr.pincoin.durian.auth.domain.Role;

import java.util.Optional;

public class RoleRepositoryImpl implements RoleRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public RoleRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Role> findRole(String code) {
        QRole role = QRole.role;


        JPAQuery<Role> contentQuery = queryFactory
                .select(role)
                .from(role)
                .where(codeEq(code));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression codeEq(String code) {
        QRole role = QRole.role;

        return code != null ? role.code.eq(code) : null;
    }
}
