package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;

public interface CategoryTreePathRepositoryQuery {
    // findRoots
    // findParent
    // findChildren
    // findAncestors
    // findDescendants

    int save(Category category);
}
