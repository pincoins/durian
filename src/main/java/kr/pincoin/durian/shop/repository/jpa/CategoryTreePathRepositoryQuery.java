package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;

import java.util.List;
import java.util.Optional;

public interface CategoryTreePathRepositoryQuery {
    // findRoots
    // findParent
    // findDescendants

    int save(Category category);

    List<CategoryTreePath> findAncestorCategories(Long id);

    List<CategoryTreePath> findChildrenCategories(Long id);

    Optional<CategoryTreePath> findParentCategory(Long id);
}
