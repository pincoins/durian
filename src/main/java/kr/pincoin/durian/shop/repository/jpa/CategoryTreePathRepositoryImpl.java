package kr.pincoin.durian.shop.repository.jpa;

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
}
