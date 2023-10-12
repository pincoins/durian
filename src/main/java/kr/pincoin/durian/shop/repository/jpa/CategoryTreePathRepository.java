package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.CategoryTreePath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryTreePathRepository
        extends JpaRepository<CategoryTreePath, Long>, CategoryTreePathRepositoryQuery {
}
