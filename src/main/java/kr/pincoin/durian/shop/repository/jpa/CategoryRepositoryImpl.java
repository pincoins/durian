package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.QCategory;
import kr.pincoin.durian.shop.domain.QCategoryTreePath;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.dto.CategorySelfParentResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QCategory.category;
import static kr.pincoin.durian.shop.domain.QCategoryTreePath.categoryTreePath;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements  CategoryRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> findCategories(Boolean isRoot, CategoryStatus status, String slug) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category)
                .where(isRootEq(isRoot),
                       statusEq(status),
                       slugContains(slug));

        return contentQuery.fetch();
    }

    @Override
    public Optional<Category> findCategory(Long id, Boolean isRoot, CategoryStatus status, String slug) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(category)
                .where(idEq(id),
                       isRootEq(isRoot),
                       statusEq(status),
                       slugContains(slug));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<CategorySelfParentResult> findSubTree(Long rootId) {
        QCategory self = new QCategory("self");
        QCategory parent = new QCategory("parent");

        QCategoryTreePath ctp1 = new QCategoryTreePath("ctp1");
        QCategoryTreePath ctp2 = new QCategoryTreePath("ctp2");

        JPAQuery<CategorySelfParentResult> contentQuery = queryFactory
                .select(Projections.fields(CategorySelfParentResult.class, self, parent))
                .from(ctp1)
                .innerJoin(ctp1.descendant, self)
                .innerJoin(ctp2)
                .on(ctp2.descendant.id.eq(self.id)) // force join
                .innerJoin(ctp2.ancestor, parent)
                .where(ctp1.ancestor.id.eq(rootId),
                       ctp2.pathLength.eq(1));

        return contentQuery.fetch();
    }

    @Override
    public List<Category> findAncestorCategories(Long id) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.ancestor, category)
                .where(categoryTreePath.descendant.id.eq(id))
                .orderBy(categoryTreePath.pathLength.desc());

        return contentQuery.fetch();
    }

    @Override
    public List<Category> findChildCategories(Long id) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.descendant, category)
                .where(categoryTreePath.ancestor.id.eq(id),
                       pathLengthEq(1))
                .orderBy(categoryTreePath.position.asc());

        return contentQuery.fetch();
    }

    @Override
    public Optional<Category> findParentCategory(Long id) {
        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.ancestor, category)
                .where(categoryTreePath.descendant.id.eq(id),
                       pathLengthEq(1));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<Category> findLeafCategories(Long id) {
        QCategoryTreePath ctp1 = new QCategoryTreePath("ctp1");
        QCategoryTreePath ctp2 = new QCategoryTreePath("ctp2");

        JPAQuery<Category> contentQuery = queryFactory
                .select(category)
                .from(ctp1)
                .innerJoin(ctp1.ancestor, category)
                .innerJoin(ctp2)
                .on(ctp1.descendant.id.eq(ctp2.descendant.id),
                    ctp2.ancestor.id.eq(id))
                .groupBy(ctp1.ancestor)
                .having(ctp1.ancestor.count().eq(1L));

        return contentQuery.fetch();
    }

    @Override
    public boolean hasPath(Long ancestorId, Long descendantId, Integer pathLength) {
        JPAQuery<Integer> countQuery = queryFactory
                .selectOne()
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

    BooleanExpression slugContains(String slug) {
        return slug != null && !slug.isBlank() ? category.slug.contains(slug) : null;
    }

    BooleanExpression isRootEq(Boolean isRoot) {
        return isRoot != null ? category.isRoot.eq(isRoot) : null;
    }

    BooleanExpression pathLengthEq(Integer pathLength) {
        return pathLength != null ? categoryTreePath.pathLength.eq(pathLength) : null;
    }
}
