package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.QCategory;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements  CategoryRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> findCategories() {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category);

        return contentQuery.fetch();
    }

    @Override
    public Optional<Category> findCategory(Long id, CategoryStatus status) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category)
                .where(idEq(id),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression idEq(Long id) {
        return id != null ? category.id.eq(id) : null;
    }

    BooleanExpression statusEq(CategoryStatus status) {
        return status != null ? category.status.eq(status) : category.status.eq(CategoryStatus.NORMAL);
    }
}
