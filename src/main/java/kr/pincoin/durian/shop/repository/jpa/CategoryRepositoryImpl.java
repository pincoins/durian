package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.QCategory;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;

import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements  CategoryRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Category> findCategories() {
        QCategory category = QCategory.category;

        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category);

        return contentQuery.fetch();
    }

    @Override
    public Optional<Category> findCategory(Long id, CategoryStatus status) {
        QCategory category = QCategory.category;

        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category)
                .where(idEq(id),
                       statusEq(status));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression idEq(Long id) {
        QCategory category = QCategory.category;

        return id != null ? category.id.eq(id) : null;
    }

    BooleanExpression statusEq(CategoryStatus status) {
        QCategory category = QCategory.category;

        return status != null ? category.status.eq(status) : category.status.eq(CategoryStatus.NORMAL);
    }
}
