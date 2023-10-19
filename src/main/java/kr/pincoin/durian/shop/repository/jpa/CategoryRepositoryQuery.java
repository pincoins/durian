package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryQuery {
    List<Category> findCategories();

    Optional<Category> findCategory(Long id, CategoryStatus status);
}
