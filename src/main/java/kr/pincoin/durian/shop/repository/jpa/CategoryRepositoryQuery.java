package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.dto.CategorySelfParentResult;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryQuery {
    List<Category> findCategories(CategoryStatus status, String slug);

    Optional<Category> findCategory(Long id, CategoryStatus status);

    Optional<Category> findCategory(String slug, CategoryStatus status);

    List<CategorySelfParentResult> findSubTree(Long rootId);

    List<Category> findAncestorCategories(Long id);

    List<Category> findChildCategories(Long id);

    Optional<Category> findParentCategory(Long id);

    List<Category> findLeafCategories(Long id);

    boolean hasPath(Long parentId, Long childId, Integer pathLength);

    boolean hasPath(Category parent, Category child, Integer pathLength);
}
