package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.pincoin.durian.shop.domain.QCategoryTreePath.categoryTreePath;

@RequiredArgsConstructor
public class CategoryTreePathRepositoryImpl implements CategoryTreePathRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CategoryTreePath> findParentAncestors(Category parent) {
        return queryFactory.select(categoryTreePath)
                .from(categoryTreePath)
                .where(categoryTreePath.descendant.eq(parent))
                .fetch();
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

    BooleanExpression pathLengthEq(Integer pathLength) {
        return pathLength != null ? categoryTreePath.pathLength.eq(pathLength) : null;
    }
}
