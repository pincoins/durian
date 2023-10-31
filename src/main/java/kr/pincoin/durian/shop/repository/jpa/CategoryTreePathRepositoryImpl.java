package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;
import kr.pincoin.durian.shop.domain.QCategory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QCategoryTreePath.categoryTreePath;

@RequiredArgsConstructor
public class CategoryTreePathRepositoryImpl implements CategoryTreePathRepositoryQuery {
    @PersistenceContext
    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public int save(Category category) {
        // JPQL does not support `INSERT INTO SELECT`, but HQL does.
        return em.createQuery(
                        "INSERT INTO" +
                                " CategoryTreePath (ancestor, descendant, pathLength, position, created, modified)" +
                                " SELECT :category, :category, 0, 0, :now, :now")
                .setParameter("category", category)
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public List<CategoryTreePath> findAncestorCategories(Long id) {
        QCategory parent = new QCategory("parent");
        QCategory child = new QCategory("child");

        JPAQuery<CategoryTreePath> contentQuery = queryFactory
                .select(categoryTreePath)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.ancestor, parent)
                .fetchJoin()
                .innerJoin(categoryTreePath.descendant, child)
                .fetchJoin()
                .where(categoryTreePath.descendant.id.eq(id))
                .orderBy(categoryTreePath.pathLength.desc());

        return contentQuery.fetch();
    }

    @Override
    public List<CategoryTreePath> findChildrenCategories(Long id) {
        QCategory parent = new QCategory("parent");
        QCategory child = new QCategory("child");

        JPAQuery<CategoryTreePath> contentQuery = queryFactory
                .select(categoryTreePath)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.ancestor, parent)
                .fetchJoin()
                .innerJoin(categoryTreePath.descendant, child)
                .fetchJoin()
                .where(categoryTreePath.ancestor.id.eq(id),
                       pathLengthEq(1))
                .orderBy(categoryTreePath.position.asc());

        return contentQuery.fetch();
    }

    @Override
    public Optional<CategoryTreePath> findParentCategory(Long id) {
        QCategory parent = new QCategory("parent");
        QCategory child = new QCategory("child");

        JPAQuery<CategoryTreePath> contentQuery = queryFactory
                .select(categoryTreePath)
                .from(categoryTreePath)
                .innerJoin(categoryTreePath.ancestor, parent)
                .fetchJoin()
                .innerJoin(categoryTreePath.descendant, child)
                .fetchJoin()
                .where(categoryTreePath.descendant.id.eq(id),
                       pathLengthEq(1));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression pathLengthEq(Integer pathLength) {
        return pathLength != null ? categoryTreePath.pathLength.eq(pathLength) : null;
    }
}
