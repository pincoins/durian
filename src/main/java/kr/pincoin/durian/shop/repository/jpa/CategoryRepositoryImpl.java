package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QCategory.category;
import static kr.pincoin.durian.shop.domain.QCategoryTreePath.categoryTreePath;

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
    public List<Category> findCategories(Boolean isRoot) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category)
                .where(isRootEq(isRoot));

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

    @Override
    public boolean hasPath(Long ancestorId, Long descendantId, Integer pathLength) {
        JPAQuery<Integer> countQuery = queryFactory.selectOne()
                .from(categoryTreePath)
                .where(categoryTreePath.ancestor.id.eq(ancestorId),
                       categoryTreePath.descendant.id.eq(descendantId),
                       pathLengthEq(pathLength));

        return countQuery.fetchFirst() != null;
    }

    @Override
    public boolean hasPath(Category parent, Category child, Integer pathLength) {
        JPAQuery<Integer> countQuery = queryFactory.selectOne()
                .from(categoryTreePath)
                .where(categoryTreePath.ancestor.id.eq(parent.getId()),
                       categoryTreePath.descendant.id.eq(child.getId()),
                       pathLengthEq(pathLength));

        return countQuery.fetchFirst() != null;
    }

    BooleanExpression idEq(Long id) {
        return id != null ? category.id.eq(id) : null;
    }

    BooleanExpression statusEq(CategoryStatus status) {
        return status != null ? category.status.eq(status) : category.status.eq(CategoryStatus.NORMAL);
    }

    BooleanExpression isRootEq(Boolean isRoot) {
        return isRoot != null ? category.isRoot.eq(isRoot) : null;
    }

    BooleanExpression pathLengthEq(Integer pathLength) {
        return pathLength != null ? categoryTreePath.pathLength.eq(pathLength) : null;
    }
}
