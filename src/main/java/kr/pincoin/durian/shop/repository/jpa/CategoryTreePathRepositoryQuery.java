package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;

import java.util.List;

public interface CategoryTreePathRepositoryQuery {
    List<CategoryTreePath> findParentAncestors(Category parent);



    // findRoots
    // findParent
    // findChildren
    // findAncestors
    // findDescendants
}
