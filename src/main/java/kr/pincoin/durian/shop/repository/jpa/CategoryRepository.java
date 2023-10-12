package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryQuery {
}
