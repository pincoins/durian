package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryTreePathRepositoryImpl implements CategoryTreePathRepositoryQuery {
    private final JPAQueryFactory queryFactory;
}
